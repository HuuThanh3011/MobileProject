package edu.hanu.a2_1901040203;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.hanu.a2_1901040203.adapters.ProductAdapter;
import edu.hanu.a2_1901040203.models.Product;
import edu.hanu.mycart.R;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rcvProduct;
    private ProductAdapter productAdapter;
    private EditText searchText;
    private List<Product> products = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        rcvProduct = findViewById(R.id.rcv_product);
        productAdapter = new ProductAdapter(MainActivity.this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        rcvProduct.setLayoutManager(gridLayoutManager);
        productAdapter.setData(products);
        rcvProduct.setAdapter(productAdapter);
        searchText = findViewById(R.id.et_searchbar);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                productAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
       new  ReadJSON().execute("https://mpr-cart-api.herokuapp.com/products");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_cart){
            Intent intent = new Intent(this,CartActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ReadJSON extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line="";
                while ((line = bufferedReader.readLine()) != null ){
                    stringBuilder.append(line);
                }
                bufferedReader.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray jsonArray = new JSONArray(s);
                for(int i = 0; i<jsonArray.length(); i++ ){
                    JSONObject objectProduct = jsonArray.getJSONObject(i);
                    int id = objectProduct.getInt("id");
                    String name= objectProduct.getString("name");
                    int unitPrice = objectProduct.getInt("unitPrice");
                    String image = objectProduct.getString("thumbnail");
                    products.add(new Product(id, name, image, unitPrice));
                }
                productAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}