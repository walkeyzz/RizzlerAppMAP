# EXPLAIN CODE PART 2
lanjutan dari part 1 yaa

# FILE ADAPTERS
Disini akan dijelasin masing-masing class dari file adapters

# FollowAdapter
```
class FollowAdapter(var context: Context, var followList: ArrayList<User>) : RecyclerView.Adapter<FollowAdapter.ViewHolder>() {
    
    // Inner class ViewHolder yang berfungsi untuk menyimpan referensi tampilan
    inner class ViewHolder(var binding: FollowRvBinding) : RecyclerView.ViewHolder(binding.root)

    // Metode ini dipanggil ketika ViewHolder baru dibuat.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Menggunakan layout inflater untuk membuat tampilan item
        var binding = FollowRvBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    // Metode ini mengembalikan jumlah item dalam daftar.
    override fun getItemCount(): Int {
        return followList.size
    }

    // Metode ini dipanggil untuk menampilkan data pada posisi tertentu.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Menggunakan Glide untuk memuat gambar profil dari URL ke ImageView di layout
        Glide.with(context)
            .load(followList[position].image)
            .placeholder(R.drawable.user)
            .into(holder.binding.profileImage)
        
        // Menetapkan teks nama pengguna pada TextView di layout
        holder.binding.name.text = followList[position].name
    }
}
```
- Deklarasi Kelas Adapter: Mendeklarasikan kelas FollowAdapter yang menggextend RecyclerView.Adapter dengan menyediakan ViewHolder untuk setiap item dalam daftar.
- Inner Class ViewHolder: Mendeklarasikan inner class ViewHolder yang berfungsi untuk menyimpan referensi tampilan (View).
- onCreateViewHolder: Metode ini dipanggil ketika ViewHolder baru dibuat. Menggunakan LayoutInflater untuk membuat tampilan item.
- getItemCount: Metode ini mengembalikan jumlah item dalam daftar.
- onBindViewHolder: Metode ini dipanggil untuk menampilkan data pada posisi tertentu. Menggunakan Glide untuk memuat gambar profil dari URL dan menetapkan teks nama pengguna pada tampilan.

# MyPostRvAdapter
```
class MyPostRvAdapter(var context: Context, var postList: ArrayList<Post>) :
    RecyclerView.Adapter<MyPostRvAdapter.ViewHolder>() {

    // Inner class ViewHolder yang berfungsi untuk menyimpan referensi tampilan
    inner class ViewHolder(var binding: MyPostRvDesignBinding) :
        RecyclerView.ViewHolder(binding.root)

    // Metode ini dipanggil ketika ViewHolder baru dibuat.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Menggunakan layout inflater untuk membuat tampilan item
        var binding = MyPostRvDesignBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    // Metode ini mengembalikan jumlah item dalam daftar.
    override fun getItemCount(): Int {
        return postList.size
    }

    // Metode ini dipanggil untuk menampilkan data pada posisi tertentu.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Menggunakan Picasso untuk memuat gambar posting dari URL ke ImageView di layout
        Picasso.get().load(postList[position].postUrl).into(holder.binding.postImage)
    }
}
```
- Deklarasi Kelas Adapter: Mendeklarasikan kelas MyPostRvAdapter yang menggextend RecyclerView.Adapter dengan menyediakan ViewHolder untuk setiap item dalam daftar.
- Inner Class ViewHolder: Mendeklarasikan inner class ViewHolder yang berfungsi untuk menyimpan referensi tampilan (View).
- onCreateViewHolder: Metode ini dipanggil ketika ViewHolder baru dibuat. Menggunakan LayoutInflater untuk membuat tampilan item.
- getItemCount: Metode ini mengembalikan jumlah item dalam daftar.
- onBindViewHolder: Metode ini dipanggil untuk menampilkan data pada posisi tertentu. Menggunakan Picasso untuk memuat gambar posting dari URL dan menetapkan gambar pada ImageView di tampilan.

# MyReelAdapter
```
class MyReelAdapter(var context: Context, var reelList: ArrayList<Reel>) :
    RecyclerView.Adapter<MyReelAdapter.ViewHolder>() {

    // Inner class ViewHolder yang berfungsi untuk menyimpan referensi tampilan
    inner class ViewHolder(var binding: MyPostRvDesignBinding) :
        RecyclerView.ViewHolder(binding.root)

    // Metode ini dipanggil ketika ViewHolder baru dibuat.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Menggunakan layout inflater untuk membuat tampilan item
        var binding = MyPostRvDesignBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    // Metode ini mengembalikan jumlah item dalam daftar.
    override fun getItemCount(): Int {
        return reelList.size
    }

    // Metode ini dipanggil untuk menampilkan data pada posisi tertentu.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Menggunakan Glide untuk memuat gambar reel dari URL ke ImageView di tampilan.
        Glide.with(context)
            .load(reelList[position].reelUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.binding.postImage)
    }
}
```
- Deklarasi Kelas Adapter: Mendeklarasikan kelas MyReelAdapter yang menggextend RecyclerView.Adapter dengan menyediakan ViewHolder untuk setiap item dalam daftar.
- Inner Class ViewHolder: Mendeklarasikan inner class ViewHolder yang berfungsi untuk menyimpan referensi tampilan (View).
- onCreateViewHolder: Metode ini dipanggil ketika ViewHolder baru dibuat. Menggunakan LayoutInflater untuk membuat tampilan item.
- getItemCount: Metode ini mengembalikan jumlah item dalam daftar.
- onBindViewHolder: Metode ini dipanggil untuk menampilkan data pada posisi tertentu. Menggunakan Glide untuk memuat gambar reel dari URL dan menetapkan gambar pada ImageView di tampilan.

# PostAdapter
```
class PostAdapter(var context: Context, var postList: ArrayList<Post>) :
    RecyclerView.Adapter<PostAdapter.MyHolder>() {

    inner class MyHolder(var binding: PostRvBinding) : RecyclerView.ViewHolder(binding.root)

    // Metode ini dipanggil ketika ViewHolder baru dibuat.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val binding = PostRvBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyHolder(binding)
    }

    // Metode ini mengembalikan jumlah item dalam daftar.
    override fun getItemCount(): Int {
        return postList.size
    }

    // Metode ini dipanggil untuk menampilkan data pada posisi tertentu.
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        try {
            // Mengambil data pengguna yang melakukan unggahan post
            Firebase.firestore.collection(USER_NODE).document(postList[position].uid)
                .get()
                .addOnSuccessListener {
                    // Mengonversi data Firestore ke objek User
                    val user = it.toObject<User>()
                    // Memuat gambar profil pengguna menggunakan Glide
                    Glide.with(context).load(user!!.image).placeholder(R.drawable.user)
                        .into(holder.binding.profileImage)
                    // Menetapkan nama pengguna pada TextView
                    holder.binding.name.text = user.name
                }
        } catch (e: Exception) {
            // Penanganan kesalahan jika terjadi
        }

        // Memuat gambar post menggunakan Glide
        Glide.with(context).load(postList[position].postUrl).placeholder(R.drawable.loading)
            .into(holder.binding.postImage)

        try {
            // Menghitung waktu sejak post dibuat menggunakan library TimeAgo
            val text = TimeAgo.using(postList[position].time.toLong())
            // Menetapkan waktu pada TextView
            holder.binding.time.text = text
        } catch (e: Exception) {
            // Penanganan kesalahan jika terjadi
            holder.binding.time.text = ""
        }

        // Menangani klik tombol "Share"
        holder.binding.share.setOnClickListener {
            // Membuat intent untuk berbagi teks
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, postList[position].postUrl)
            // Memulai aktivitas berbagi
            context.startActivity(intent)
        }

        // Menangani klik tombol "Like"
        holder.binding.like.setOnClickListener {
            // Mengubah ikon "Like" menjadi ikon hati berwarna merah
            holder.binding.like.setImageResource(R.drawable.heart_like)
        }

        // Menetapkan keterangan post pada TextView
        holder.binding.csption.text = postList[position].caption
    }
}
```
- Deklarasi Kelas Adapter: Mendeklarasikan kelas PostAdapter yang menggextend RecyclerView.Adapter dengan menyediakan ViewHolder untuk setiap item dalam daftar.
- Inner Class ViewHolder: Mendeklarasikan inner class MyHolder yang berfungsi untuk menyimpan referensi tampilan (View).
- onCreateViewHolder: Metode ini dipanggil ketika ViewHolder baru dibuat. Menggunakan LayoutInflater untuk membuat tampilan item.
- getItemCount: Metode ini mengembalikan jumlah item dalam daftar.
- onBindViewHolder: Metode ini dipanggil untuk menampilkan data pada posisi tertentu. Menggunakan Glide untuk memuat gambar profil dan post dari URL. Menangani klik tombol "Share" dan "Like".

# ReelAdapter
```
class ReelAdapter(var context: Context, var reelList: ArrayList<Reel>) :
    RecyclerView.Adapter<ReelAdapter.ViewHolder>() {

    inner class ViewHolder(var binding: ReelDgBinding) : RecyclerView.ViewHolder(binding.root)

    // Metode ini dipanggil ketika ViewHolder baru dibuat.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ReelDgBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    // Metode ini mengembalikan jumlah item dalam daftar.
    override fun getItemCount(): Int {
        return reelList.size
    }

    // Metode ini dipanggil untuk menampilkan data pada posisi tertentu.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Memuat gambar profil menggunakan Picasso
        Picasso.get().load(reelList[position].profileLink).placeholder(R.drawable.user)
            .into(holder.binding.profileImage)
        // Menetapkan keterangan reel pada TextView
        holder.binding.caption.text = reelList[position].caption

        // Menetapkan sumber video untuk VideoView
        holder.binding.videoView.setVideoPath(reelList[position].reelUrl)

        // Menangani peristiwa saat persiapan pemutaran video
        holder.binding.videoView.setOnPreparedListener {
            // Menyembunyikan ProgressBar setelah video siap diputar
            holder.binding.progressBar.visibility = View.GONE
            // Memulai pemutaran video
            holder.binding.videoView.start()
        }
    }
}
```
- Deklarasi Kelas Adapter: Mendeklarasikan kelas ReelAdapter yang menggextend RecyclerView.Adapter dengan menyediakan ViewHolder untuk setiap item dalam daftar.
- Inner Class ViewHolder: Mendeklarasikan inner class ViewHolder yang berfungsi untuk menyimpan referensi tampilan (View).
- onCreateViewHolder: Metode ini dipanggil ketika ViewHolder baru dibuat. Menggunakan LayoutInflater untuk membuat tampilan item.
- getItemCount: Metode ini mengembalikan jumlah item dalam daftar.
- onBindViewHolder: Metode ini dipanggil untuk menampilkan data pada posisi tertentu. Menggunakan Picasso untuk memuat gambar profil dan menetapkan sumber video untuk VideoView. Menangani peristiwa saat persiapan pemutaran video.

# SearchAdapter
```
class SearchAdapter(var context: Context, var userList: ArrayList<User>) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    // Inner class ViewHolder untuk menyimpan referensi tampilan (View)
    inner class ViewHolder(var binding: SearchRvBinding) : RecyclerView.ViewHolder(binding.root)

    // Metode ini dipanggil ketika ViewHolder baru dibuat.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SearchRvBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    // Metode ini mengembalikan jumlah item dalam daftar.
    override fun getItemCount(): Int {
        return userList.size
    }

    // Metode ini dipanggil untuk menampilkan data pada posisi tertentu.
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Variabel untuk menentukan apakah pengguna diikuti atau tidak
        var isFollow = false

        // Memuat gambar profil menggunakan Glide
        Glide.with(context).load(userList[position].image).placeholder(R.drawable.user)
            .into(holder.binding.profileImage)

        // Menetapkan nama pengguna pada TextView
        holder.binding.name.text = userList[position].name

        // Mengecek apakah pengguna sudah diikuti
        Firebase.firestore.collection("${Firebase.auth.currentUser!!.uid}$FOLLOW")
            .whereEqualTo("email", userList[position].email).get().addOnSuccessListener {

                // Jika tidak ada dokumen yang cocok, pengguna belum diikuti
                if (it.documents.size == 0) {
                    isFollow = false
                } else {
                    // Jika ada dokumen, pengguna sudah diikuti
                    holder.binding.follow.text = "Unfollow"
                    isFollow = true
                }
            }

        // Menangani klik tombol follow/unfollow
        holder.binding.follow.setOnClickListener {
            if (isFollow) {
                // Jika pengguna sudah diikuti, hapus dokumen yang sesuai
                Firebase.firestore.collection("${Firebase.auth.currentUser!!.uid}$FOLLOW")
                    .whereEqualTo("email", userList[position].email).get().addOnSuccessListener {

                        Firebase.firestore.collection("${Firebase.auth.currentUser!!.uid}$FOLLOW")
                            .document(it.documents[0].id).delete()
                        holder.binding.follow.text = "Follow"
                        isFollow = false
                    }
            } else {
                // Jika pengguna belum diikuti, tambahkan dokumen baru
                Firebase.firestore.collection("${Firebase.auth.currentUser!!.uid}$FOLLOW")
                    .document().set(userList[position])
                holder.binding.follow.text = "Unfollow"
                isFollow = true
            }
        }
    }
}
```
- Deklarasi Kelas Adapter: Mendeklarasikan kelas SearchAdapter yang menggextend RecyclerView.Adapter dengan menyediakan ViewHolder untuk setiap item dalam daftar.
- Inner Class ViewHolder: Mendeklarasikan inner class ViewHolder yang berfungsi untuk menyimpan referensi tampilan (View).
- onCreateViewHolder: Metode ini dipanggil ketika ViewHolder baru dibuat. Menggunakan LayoutInflater untuk membuat tampilan item.
- getItemCount: Metode ini mengembalikan jumlah item dalam daftar.
- onBindViewHolder: Metode ini dipanggil untuk menampilkan data pada posisi tertentu. Menggunakan Glide untuk memuat gambar profil. Menetapkan nama pengguna pada TextView. Mengecek apakah pengguna sudah diikuti atau belum. Menangani klik tombol follow/unfollow.

# ViewPagerAdapter
```
/ Kelas adapter untuk mengelola Fragments dalam ViewPager
class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    // Daftar Fragments yang akan ditampilkan dalam ViewPager
    val fragmentList = mutableListOf<Fragment>()

    // Daftar judul untuk setiap Fragment
    val titleList = mutableListOf<String>()

    // Mendapatkan jumlah Fragments yang akan ditampilkan
    override fun getCount(): Int {
        return fragmentList.size
    }

    // Mendapatkan Fragment pada posisi tertentu
    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    // Mendapatkan judul untuk setiap Fragment
    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }

    // Menambahkan Fragment dan judul baru ke dalam daftar
    fun addFragments(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        titleList.add(title)
    }
}
```
- Deklarasi Kelas Adapter: Mendeklarasikan kelas ViewPagerAdapter yang menggextend FragmentPagerAdapter. FragmentPagerAdapter digunakan untuk mengelola Fragments dalam ViewPager.
- Variabel dan Daftar Fragments: Membuat variabel dan daftar untuk menyimpan Fragments dan judulnya.
- getCount: Metode ini mengembalikan jumlah Fragments yang akan ditampilkan.
- getItem: Metode ini mengembalikan Fragment pada posisi tertentu.
- getPageTitle: Metode ini mengembalikan judul untuk setiap Fragment.
- addFragments: Metode ini digunakan untuk menambahkan Fragment dan judul baru ke dalam daftar.




