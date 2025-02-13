package registerationlogin.repository;

import registerationlogin.entity.Order;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByRestaurantId(Long restaurantId);

    @Query("SELECT COALESCE(SUM(o.totalPrice), 0.0) " +
            "FROM Order o " +
            "WHERE o.orderDate >= :start " +
            "AND o.orderDate < :end " +
            "AND o.restaurant.id = :restaurantId")
    Double findTotalSalesByDateRangeAndRestaurant(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("restaurantId") Long restaurantId);

    // @Query("SELECT SUM(o.totalPrice) FROM food_order o WHERE DATE(o.orderDate) =
    // CURRENT_DATE")
    // Double getTodaySales();

    // @Query("SELECT (SUM(o.totalPrice) - LAG(SUM(o.totalPrice), 1) OVER (ORDER BY
    // DATE(o.orderDate))) / LAG(SUM(o.totalPrice), 1) OVER (ORDER BY
    // DATE(o.orderDate)) * 100 FROM food_order o WHERE DATE(o.orderDate) BETWEEN
    // CURRENT_DATE - 1 AND CURRENT_DATE")
    // Double getSalesPercentageChange();

    // @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'ACTIVE'")
    // Integer countActiveOrders();

    // @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'PREPARING'")
    // Integer countPreparingOrders();

    // @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'READY'")
    // Integer countReadyOrders();

    // @Query("SELECT COUNT(DISTINCT o.tableNumber) FROM Order o WHERE o.status NOT
    // IN ('COMPLETED', 'CANCELLED')")
    // Integer countOccupiedTables();

    // @Query("SELECT AVG(r.rating) FROM Review r")
    // Double getAverageRating();

    // @Query("SELECT COUNT(r) FROM Review r")
    // Integer countReviews();

    // @Query("SELECT DATE(o.orderDate), SUM(o.totalAmount) FROM Order o WHERE
    // o.orderDate >= CURRENT_DATE - 7 GROUP BY DATE(o.orderDate) ORDER BY
    // DATE(o.orderDate)")
    // List<Object[]> getSalesTrendForWeek();

    // @Query("SELECT i.name, SUM(oi.quantity) FROM OrderItem oi JOIN oi.item i
    // GROUP BY i.name ORDER BY SUM(oi.quantity) DESC")
    // List<Object[]> getTopSellingItems();

}
