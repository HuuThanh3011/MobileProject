package edu.hanu.a2_1901040203.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import edu.hanu.a2_1901040203.models.Product;
import edu.hanu.a2_1901040203.database.CartDatabase;
import edu.hanu.a2_1901040203.R;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> implements Filterable {
    private CartDatabase cartDB;
    private Context context;
    private List<Product> productList;
    private List<Product> productListOld;

    public ProductAdapter(Context context) {
        this.context = context;
        this.cartDB = new CartDatabase(context);
    }
    public void setData(List<Product> products){
        this.productList = products;
        this.productListOld = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.nameProduct.setText(product.getName());
        holder.priceProduct.setText(Integer.toString(product.getUnitPrice()));
        new LoadImageInternet(holder.imgProduct).execute(product.getThumbnail());
        holder.addToCartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newQ = product.getQuantity() + 1;
                product.setQuantity(newQ);
                if (product.getQuantity() == 1){
                    boolean success = cartDB.addToCart(product);
                    Toast.makeText(context, "Success =" + success,Toast.LENGTH_SHORT).show();
                }
                else {
                    cartDB.updateDatabase(product);
                    Toast.makeText(context, "Quantity = "+ product.getQuantity(),Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        if (productList != null){
            return productList.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String keyword = charSequence.toString();
                if (keyword.isEmpty()){
                    productList = productListOld;
                }else {
                    List<Product> list = new ArrayList<>();
                    for (Product product:
                         productListOld) {
                        if(product.getName().toLowerCase().contains(keyword.toLowerCase())){
                            list.add(product);
                        }
                    }
                    productList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = productList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productList=(List<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgProduct;
        private TextView nameProduct;
        private TextView priceProduct;
        private ImageButton addToCartbtn;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_product);
            nameProduct = itemView.findViewById(R.id.tv_name);
            priceProduct = itemView.findViewById(R.id.tv_price);
            addToCartbtn = itemView.findViewById(R.id.img_btn_add_cart);

        }
    }
    private class LoadImageInternet extends AsyncTask<String, Void, Bitmap>{
        private ImageView imageView;
        private Bitmap bitmap = null;
        public LoadImageInternet(ImageView imageView){
            this.imageView = imageView;
        }
        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                InputStream inputStream = url.openConnection().getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }
}
