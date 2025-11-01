package com.example.canteen.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    public enum Status { PLACED, PREPARING, READY, COMPLETED, CANCELLED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant createdAt = Instant.now();

    // ✅ Student who placed the order
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // ✅ Vendor who will fulfill the order
    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private User vendor;

    private BigDecimal total = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PLACED;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    public Order() {}

    // ✅ Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public User getVendor() { return vendor; }   // ✅ new getter
    public void setVendor(User vendor) { this.vendor = vendor; } // ✅ new setter

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }
}
