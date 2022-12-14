package edu.hanu.a2_1901040203;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.hanu.a2_1901040203.adapters.ProductCartAdapter;
import edu.hanu.a2_1901040203.models.Product;
import edu.hanu.a2_1901040203.R;

public class CartActivity extends AppCompatActivity {
    private RecyclerView rcv_cart;
    private ProductCartAdapter productCartAdapter;
    private TextView total;
    List<Product> productList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);
        rcv_cart = findViewById(R.id.rcv_cart);
        productCartAdapter = new ProductCartAdapter(CartActivity.this);
        productList = productCartAdapter.getCartDatabase().getALlProductCart();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcv_cart.setLayoutManager(linearLayoutManager);
        productCartAdapter.setData(productList);
        rcv_cart.setAdapter(productCartAdapter);
        total = findViewById(R.id.tv_total);
        total.setText(String.valueOf(getTotalPrice(productList)));
    }
    public int getTotalPrice(List<Product> productList){
        int sum = 0;
        for (Product p:
                productList) {
            sum += (p.getUnitPrice() * p.getQuantity());
        }
        return sum;
    }
}