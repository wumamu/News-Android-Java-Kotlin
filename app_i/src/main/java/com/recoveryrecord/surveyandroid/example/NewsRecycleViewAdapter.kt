package com.recoveryrecord.surveyandroid.example

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.recoveryrecord.surveyandroid.example.model.News
import java.text.SimpleDateFormat

class NewsRecycleViewAdapter(
    private val dataModelArrayList: ArrayList<News>,
    private val context: Context
): RecyclerView.Adapter<NewsRecycleViewAdapter.ViewHolder>() {
    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // passing our layout file for displaying our card item
        return ViewHolder(
            LayoutInflater.from(
                context
            ).inflate(R.layout.news_rv_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // setting data to our views in Recycler view items.
        val model = dataModelArrayList[position]
        holder.newsTitle.text = model.title
        model.pubDate?.toDate()?.let { date ->
            @SuppressLint("SimpleDateFormat") val formatter = SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z")
            val my_tt: List<String> = ArrayList(
                listOf(
                    *formatter.format(date).split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()))
            holder.newsPubTime.text = String.format("%s %s", my_tt[0], my_tt[2])
        }


        holder.newsImg.visibility = View.GONE
        //we don't need img
//        if(model.getImage()!=null && model.getImage()!="NA"){
//            new DownloadImageTask(holder.newsImg).execute(model.getImage());
//            holder.newsImg.setAdjustViewBounds(true);
//            holder.newsImg.setMaxHeight(200);
//        } else {
//            holder.newsImg.setVisibility(View.GONE);
////            new DownloadImageTask(holder.newsImg).execute("https://im.marieclaire.com.tw/s1920c1080h100b0/assets/mc/202101/601272789955C1611821688.jpeg");
//        }


//        holder.newsImg.set;
        var media_name = model.media //from data base
        when (media_name) {
            "cna" -> media_name = "中央社"
            "chinatimes" -> media_name = "中時"
            "cts" -> media_name = "華視"
            "ebc" -> media_name = "東森"
            "ltn" -> media_name = "自由時報"
            "storm" -> media_name = "風傳媒"
            "udn" -> media_name = "聯合"
            "ettoday" -> media_name = "ettoday"
            "setn" -> media_name = "三立"
            else -> {}
        }
        holder.newsMedia.text = media_name
        //        if(model.getPubdate()==null){
//            FirebaseFirestore db = FirebaseFirestore.getInstance();
//            DocumentReference docRef = db.collection("server_push_notifications").document(model.getId());
//            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        assert document != null;
//                        if (document.exists()) {
//                            Date date = document.getTimestamp("pubdate").toDate();
//                            @SuppressLint("SimpleDateFormat")
//                            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//                            List<String> my_tt = new ArrayList<String>(Arrays.asList(formatter.format(date).split(" ")));
//                            holder.newsPubTime.setText(String.format("%s %s", my_tt.get(0), my_tt.get(2)));
//                            holder.newsMedia.setText(document.getString("media"));
//                        } else {
//                            Log.d("lognewsselect", "No such document");
//                        }
//                    } else {
//                        Log.d("lognewsselect", "get failed with ", task.getException());
//                    }
//                }
//            });
//        } else {
//            Date date = model.getPubdate().toDate();
//            @SuppressLint("SimpleDateFormat")
//            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//            List<String> my_tt = new ArrayList<String>(Arrays.asList(formatter.format(date).split(" ")));
//            holder.newsPubTime.setText(String.format("%s %s", my_tt.get(0), my_tt.get(2)));
//            holder.newsMedia.setText(model.getMedia());
//        }


        // we are using Picasso to load images
        // from URL inside our image view.
//        Picasso.get().load(modal.getImgUrl()).into(holder.courseIV);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // setting on click listener
//                // for our items of recycler items.
//                Toast.makeText(context, "Clicked item is " + modal.getName(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    override fun getItemCount(): Int {
        // returning the size of array list.
        return dataModelArrayList.size
    }

    inner class ViewHolder @RequiresApi(api = Build.VERSION_CODES.O) constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        // creating variables for our
        // views of recycler items.
        val newsTitle: TextView
        val newsPubTime: TextView
        val newsMedia: TextView
        val newsImg: ImageView

        //        private ImageView courseIV;
        init {
            // initializing the views of recycler views.
            newsTitle = itemView.findViewById(R.id.text_view_title)
            newsPubTime = itemView.findViewById(R.id.text_view_pubtime)
            newsMedia = itemView.findViewById(R.id.text_view_media)
            newsImg = itemView.findViewById(R.id.imgView)

//            courseIV = itemView.findViewById(R.id.idIVimage);
//            itemView.setCardBackgroundColor(Color.parseColor("#e0efff"));
            // 點擊項目時
            itemView.setOnClickListener { view: View? ->
//                    Toast.makeText(view.getContext(), "click " +getAdapterPosition(),Toast.LENGTH_SHORT).show();
                val (_, media, id) = dataModelArrayList[adapterPosition]
                val intent = Intent()
                intent.setClass(context, NewsModuleActivity::class.java)
                intent.putExtra("trigger_by", "self_trigger")
                intent.putExtra("news_id", id)
                intent.putExtra("media", media)
                context.startActivity(intent)
            }
        }
    }

    //image download ###############################################################################
//    internal class DownloadImageTask(var bmImage: ImageView) :
//        AsyncTask<String?, Void?, Bitmap?>() {
//        protected override fun doInBackground(vararg urls: String): Bitmap? {
//            val urldisplay = urls[0]
//            var mIcon11: Bitmap? = null
//            try {
//                val `in` = URL(urldisplay).openStream()
//                mIcon11 = BitmapFactory.decodeStream(`in`)
//            } catch (e: Exception) {
//                Log.e("Error", e.message!!)
//                e.printStackTrace()
//            }
//            return mIcon11
//        }
//
//        override fun onPostExecute(result: Bitmap?) {
//            bmImage.setImageBitmap(result)
//        }
//    }
}