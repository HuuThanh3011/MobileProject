package edu.hanu.a2_1901040203.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;

import edu.hanu.a2_1901040203.R;
import edu.hanu.a2_1901040203.database.CartDatabase;
import edu.hanu.a2_1901040203.models.Product;

public class ProductCartAdapter extends RecyclerView.Adapter<ProductCartAdapter.ProductCartViewHolder> {
    private Context context;
    private List<Product> productList;
    private CartDatabase cartDatabase;

    public CartDatabase getCartDatabase() {
        return cartDatabase;
    }

    public void setCartDatabase(CartDatabase cartDatabase) {
        this.cartDatabase = cartDatabase;
    }

    public ProductCartAdapter(Context context) {
        this.context = context;
        cartDatabase = new CartDatabase(context);
    }
    public void setData(List<Product> products){
        this.productList = products;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ProductCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_cart, parent, false);
        return new ProductCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCartViewHolder holder, int position) {
        Product product =  productList.get(position);
        holder.nameProductCart.setText(product.getName());
        holder.priceProductCart.setText("đ "+Integer.toString(product.getUnitPrice()));
        holder.quantityProductCart.setText(Integer.toString(product.getQuantity()));
        holder.sumPrice.setText(Integer.toString(product.getQuantity() * product.getUnitPrice()));
        new LoadImageInternet(holder.thumbProductCart).execute(product.getThumbnail());
        holder.addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newQ = product.getQuantity() + 1;
                product.setQuantity(newQ);
                cartDatabase.updateDatabase(product);
                notifyItemChanged(holder.getAdapterPosition());
                holder.quantityProductCart.setText(Integer.toString(product.getQuantity()));
                holder.sumPrice.setText(Integer.toString(product.getQuantity() * product.getUnitPrice()));
                Toast.makeText(context, "Quantity = "+ product.getQuantity(),Toast.LENGTH_SHORT).show();
            }
        });
        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newQ = product.getQuantity() -1;
                product.setQuantity(newQ);
                cartDatabase.updateDatabase(product);
                holder.quantityProductCart.setText(Integer.toString(product.getQuantity()));
                holder.sumPrice.setText(Integer.toString(product.getQuantity() * product.getUnitPrice()));
                if (product.getQuantity() == 0){
                    cartDatabase.deleteProduct(product);
                    productList.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
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

    public class ProductCartViewHolder extends RecyclerView.ViewHolder{
        private ImageView thumbProductCart;
        private TextView nameProductCart;
        private TextView priceProductCart;
        private TextView quantityProductCart;
        private  TextView sumPrice;
        private ImageButton addbtn;
        private ImageButton deletebtn;

        public ProductCartViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbProductCart = itemView.findViewById(R.id.iv_thumb_product_cart);
            nameProductCart = itemView.findViewById(R.id.tv_name_product_cart);
            priceProductCart = itemView.findViewById(R.id.tv_price_product_cart);
            quantityProductCart = itemView.findViewById(R.id.tv_quantity_product_cart);
            sumPrice = itemView.findViewById(R.id.tv_sum_price_product_cart);
            addbtn = itemView.findViewById(R.id.add_qproduct_btn);
            deletebtn = itemView.findViewById(R.id.delete_qproduct_btn);

        }
    }
    private class LoadImageInternet extends AsyncTask<String, Void, Bitmap> {
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
