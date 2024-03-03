package by.bsuir.lookmanager.services.impl;

import by.bsuir.lookmanager.dao.ProductRepository;
import by.bsuir.lookmanager.dao.UserRepository;
import by.bsuir.lookmanager.dto.ApplicationResponseDto;
import by.bsuir.lookmanager.dto.product.general.GeneralProductResponseDto;
import by.bsuir.lookmanager.dto.product.general.mapper.ProductListMapper;
import by.bsuir.lookmanager.entities.product.ProductEntity;
import by.bsuir.lookmanager.entities.user.UserEntity;
import by.bsuir.lookmanager.services.RecommendedService;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.model.GenericBooleanPrefDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RecommendedServiceImpl implements RecommendedService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductListMapper productResponseMapper;

    @Override
    public ApplicationResponseDto<List<GeneralProductResponseDto>> findRecommendedProducts(Long userId, Long numberOfRecommendedItems) {
        ApplicationResponseDto<List<GeneralProductResponseDto>> responseDto = new ApplicationResponseDto<>();
        try {
            DataModel model = buildDataModel();
            RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
                public Recommender buildRecommender(DataModel model) throws TasteException {
                    //ItemSimilarity similarity = new EuclideanDistanceSimilarity(model);
                    ItemSimilarity similarity = new PearsonCorrelationSimilarity(model);

                    //Optimizer optimizer = new NonNegativeQuadraticOptimizer();
                    return new GenericBooleanPrefItemBasedRecommender(model, similarity);
                }
            };

            Recommender recommender = recommenderBuilder.buildRecommender(model);
            List<RecommendedItem> recommendations = recommender.recommend(userId, Math.toIntExact(numberOfRecommendedItems));

            List<ProductEntity> recommendedProducts = new ArrayList<>();

            List<ProductEntity> allProducts = productRepository.findAll();

            for (RecommendedItem recommendedItem : recommendations) {
                long productId = recommendedItem.getItemID();
                allProducts.stream()
                        .filter(product -> product.getId() == productId)
                        .findFirst().ifPresent(recommendedProducts::add);
            }
            responseDto.setPayload(productResponseMapper.toGeneralProductResponseDtoList(recommendedProducts));
        } catch (TasteException e) {
            e.printStackTrace();
        }
        return responseDto;
    }

    private DataModel buildDataModel() {
        FastByIDMap<FastIDSet> userData = new FastByIDMap<>();

        List<UserEntity> users = (List<UserEntity>) userRepository.findAll();

        for (UserEntity user : users) {
            FastIDSet preferences = new FastIDSet();
            for (ProductEntity product : user.getFavouriteProducts()) {
                preferences.add(product.getId());
            }
            userData.put(user.getId(), preferences);
        }

        return new GenericBooleanPrefDataModel(userData);
    }
}
