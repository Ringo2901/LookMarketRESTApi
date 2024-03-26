package by.bsuir.lookmanager.recomended;

import by.bsuir.lookmanager.entities.product.ProductEntity;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductSimilarityCalculator {
    private final Directory index;
    private IndexSearcher searcher;
    private final Analyzer analyzer;
    private static final Map<String, Integer> importanceCoefficients = new HashMap<>();

    static {
        importanceCoefficients.put("title", 1);
        importanceCoefficients.put("subCategory", 1);
        importanceCoefficients.put("gender", 1);
        importanceCoefficients.put("condition", 1);
        importanceCoefficients.put("productBrand", 1);
        importanceCoefficients.put("description", 1);
        importanceCoefficients.put("size", 1);
        importanceCoefficients.put("color", 1);
        importanceCoefficients.put("material", 1);
        importanceCoefficients.put("tag", 1);
        importanceCoefficients.put("season", 1);
        importanceCoefficients.put("ageType", 1);
    }

    public ProductSimilarityCalculator() throws IOException {
        index = new RAMDirectory();
        analyzer = new SimpleAnalyzer();
    }

    public void initializeIndex(List<ProductEntity> entities) throws IOException {
        for (ProductEntity entity : entities) {
            addProductToIndex(entity);
        }
    }

    public void addProductToIndex(ProductEntity product) throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        try (IndexWriter writer = new IndexWriter(index, config)) {
            Document doc = new Document();
            FieldType fieldType = new FieldType();
            fieldType.setStored(true);
            fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
            fieldType.setTokenized(true);
            doc.add(new Field("text", product.toText(importanceCoefficients), fieldType));
            writer.addDocument(doc);
        }
    }

    private void initializeSearcher() throws IOException {
        this.searcher = new IndexSearcher(DirectoryReader.open(index));
    }

    public double calculateSimilarity(ProductEntity product1, ProductEntity product2) throws IOException {
        if (searcher == null) {
            initializeSearcher();
        }

        // Получаем текстовые представления продуктов
        String text1 = product1.toText(importanceCoefficients);
        String text2 = product2.toText(importanceCoefficients);

        // Преобразуем текст в векторы TF-IDF
        Map<String, Double> vector1 = convertToTFIDFVector(text1);
        Map<String, Double> vector2 = convertToTFIDFVector(text2);

        // Вычисляем косинусное сходство между векторами
        double cosineSimilarity = calculateCosineSimilarity(vector1, vector2);

        return cosineSimilarity;
    }

    private double calculateCosineSimilarity(Map<String, Double> vector1, Map<String, Double> vector2) {
        double dotProduct = 0;
        double magnitude1 = 0;
        double magnitude2 = 0;

        // Вычисляем скалярное произведение и нормы векторов
        for (Map.Entry<String, Double> entry : vector1.entrySet()) {
            String term = entry.getKey();
            double tfidf1 = entry.getValue();
            double tfidf2 = vector2.getOrDefault(term, 0.0);

            dotProduct += tfidf1 * tfidf2;
            magnitude1 += Math.pow(tfidf1, 2);
        }
        for (Map.Entry<String, Double> entry : vector2.entrySet()) {
            magnitude2 += Math.pow(entry.getValue(), 2);
        }

        // Вычисляем косинусное сходство
        if (magnitude1 == 0 || magnitude2 == 0) {
            return 0; // Если векторы нулевые, возвращаем 0
        } else {
            return dotProduct / (Math.sqrt(magnitude1) * Math.sqrt(magnitude2));
        }
    }


    private Map<String, Double> convertToTFIDFVector(String text) throws IOException {
        Map<String, Double> tfidfVector = new HashMap<>();

        // Создаем токенизатор для анализа текста
        try (TokenStream tokenStream = analyzer.tokenStream("text", new StringReader(text))) {
            CharTermAttribute termAttribute = tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();

            // Считаем TF для каждого терма
            int totalTerms = 0;
            Map<String, Integer> termFrequency = new HashMap<>();
            while (tokenStream.incrementToken()) {
                String term = termAttribute.toString();
                termFrequency.put(term, termFrequency.getOrDefault(term, 0) + 1);
                totalTerms++;
            }

            // Считаем IDF для каждого терма
            IndexReader indexReader = DirectoryReader.open(index);
            int numDocs = indexReader.numDocs();
            for (Map.Entry<String, Integer> entry : termFrequency.entrySet()) {
                String term = entry.getKey();
                double tf = (double) entry.getValue() / totalTerms;
                double idf = Math.log((double) numDocs / (indexReader.docFreq(new Term("text", term)) + 1)); // +1 для избежания деления на 0
                tfidfVector.put(term, tf * idf);
            }

            tokenStream.end();
        }

        return tfidfVector;
    }
}