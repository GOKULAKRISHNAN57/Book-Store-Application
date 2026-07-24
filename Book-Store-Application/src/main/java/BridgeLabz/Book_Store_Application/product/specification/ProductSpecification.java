package BridgeLabz.Book_Store_Application.product.specification;

import BridgeLabz.Book_Store_Application.product.dto.ProductFilterRequest;
import BridgeLabz.Book_Store_Application.product.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    private ProductSpecification() {
    }

    public static Specification<Product> buildSpecification(
            ProductFilterRequest request){

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Keyword Search
            if (request.getKeyword() != null &&
                    !request.getKeyword().isBlank()) {

                String keyword =
                        "%" + request.getKeyword().toLowerCase() + "%";

                predicates.add(

                        criteriaBuilder.or(

                                criteriaBuilder.like(
                                        criteriaBuilder.lower(
                                                root.get("title")
                                        ),
                                        keyword
                                ),

                                criteriaBuilder.like(
                                        criteriaBuilder.lower(
                                                root.get("author")
                                        ),
                                        keyword
                                ),

                                criteriaBuilder.like(
                                        criteriaBuilder.lower(
                                                root.get("isbn")
                                        ),
                                        keyword
                                )

                        )

                );
            }

            // Category
            if (request.getCategoryId() != null) {

                predicates.add(

                        criteriaBuilder.equal(
                                root.get("category").get("id"),
                                request.getCategoryId()
                        )

                );

            }

            // Minimum Price
            if (request.getMinPrice() != null) {

                predicates.add(

                        criteriaBuilder.greaterThanOrEqualTo(
                                root.get("price"),
                                request.getMinPrice()
                        )

                );

            }

            // Maximum Price
            if (request.getMaxPrice() != null) {

                predicates.add(

                        criteriaBuilder.lessThanOrEqualTo(
                                root.get("price"),
                                request.getMaxPrice()
                        )

                );

            }

            // Active
            if (request.getActive() != null) {

                predicates.add(

                        criteriaBuilder.equal(
                                root.get("active"),
                                request.getActive()
                        )

                );

            }

            return criteriaBuilder.and(
                    predicates.toArray(new Predicate[0])
            );

        };
    }

}