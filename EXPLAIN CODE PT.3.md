# EXPLAIN CODE PART 3
lanjutan dari yang sebelumnyaa

# FILE MODELS
Pada bagian ini akan dijelaskan kode masing-masing class dari file models

# Post
```
// Kelas Model untuk Representasi Data Post
class Post {

    // Properti postUrl, caption, uid, dan time
    var postUrl: String = ""
    var caption: String = ""
    var uid: String = ""
    var time: String = ""

    // Konstruktor kosong (tanpa parameter)
    constructor()

    // Konstruktor dengan parameter postUrl dan caption
    constructor(postUrl: String, caption: String) {
        this.postUrl = postUrl
        this.caption = caption
    }

    // Konstruktor dengan parameter postUrl, caption, uid, dan time
    constructor(postUrl: String, caption: String, uid: String, time: String) {
        this.postUrl = postUrl
        this.caption = caption
        this.uid = uid
        this.time = time
    }
}
```
- Kelas Model Post: Mendefinisikan kelas Post sebagai model untuk representasi data post.
- Properti: Membuat properti postUrl, caption, uid, dan time yang akan digunakan untuk menyimpan informasi post.
- Konstruktor Kosong: Menyediakan konstruktor kosong tanpa parameter.
- Konstruktor Dengan Parameter 1: Konstruktor dengan parameter postUrl dan caption untuk inisialisasi properti.
- Konstruktor Dengan Parameter 2: Konstruktor dengan parameter postUrl, caption, uid, dan time untuk inisialisasi properti dengan data lebih lengkap.

# Reel
```
// Kelas Model untuk Representasi Data Reel
class Reel {

    // Properti reelUrl, caption, dan profileLink
    var reelUrl: String = ""
    var caption: String = ""
    var profileLink: String? = null

    // Konstruktor kosong (tanpa parameter)
    constructor()

    // Konstruktor dengan parameter reelUrl dan caption
    constructor(reelUrl: String, caption: String) {
        this.reelUrl = reelUrl
        this.caption = caption
    }

    // Konstruktor dengan parameter reelUrl, caption, dan profileLink
    constructor(reelUrl: String, caption: String, profileLink: String) {
        this.reelUrl = reelUrl
        this.caption = caption
        this.profileLink = profileLink
    }
}
```
- Kelas Model Reel: Mendefinisikan kelas Reel sebagai model untuk representasi data reel.
- Properti: Membuat properti reelUrl, caption, dan profileLink yang akan digunakan untuk menyimpan informasi reel.
- Konstruktor Kosong: Menyediakan konstruktor kosong tanpa parameter.
- Konstruktor Dengan Parameter 1: Konstruktor dengan parameter reelUrl dan caption untuk inisialisasi properti.
- Konstruktor Dengan Parameter 2: Konstruktor dengan parameter reelUrl, caption, dan profileLink untuk inisialisasi properti dengan data lebih lengkap.

# User
```
// Kelas Model untuk Representasi Data User
class User {

    // Properti image, name, email, dan password
    var image: String? = null
    var name: String? = null
    var email: String? = null
    var password: String? = null

    // Konstruktor kosong (tanpa parameter)
    constructor()

    // Konstruktor dengan parameter image, name, email, dan password
    constructor(image: String?, name: String?, email: String?, password: String?) {
        this.image = image
        this.name = name
        this.email = email
        this.password = password
    }

    // Konstruktor dengan parameter name, email, dan password
    constructor(name: String?, email: String?, password: String?) {
        this.name = name
        this.email = email
        this.password = password
    }

    // Konstruktor dengan parameter email dan password
    constructor(email: String?, password: String?) {
        this.email = email
        this.password = password
    }
}
```
- Kelas Model User: Mendefinisikan kelas User sebagai model untuk representasi data pengguna.
- Properti: Membuat properti image, name, email, dan password yang akan digunakan untuk menyimpan informasi pengguna.
- Konstruktor Kosong: Menyediakan konstruktor kosong tanpa parameter.
- Konstruktor Dengan Parameter 1: Konstruktor dengan parameter image, name, email, dan password untuk inisialisasi properti dengan data lengkap.
- Konstruktor Dengan Parameter 2: Konstruktor dengan parameter name, email, dan password untuk inisialisasi properti tanpa gambar.
- Konstruktor Dengan Parameter 3: Konstruktor dengan parameter email dan password untuk inisialisasi properti tanpa nama dan gambar.

# FILE POST
Pada bagian ini akan dijelaskan kode masing-masing class dari file post

# PostActivity
```
class PostActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityPostBinding.inflate(layoutInflater)
    }
    var imageUrl: String? = null

    // ActivityResultLauncher untuk mendapatkan konten gambar dari penyimpanan perangkat
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            // Fungsi uploadImage mengunggah gambar ke penyimpanan Firebase dan memberikan URL gambar
            uploadImage(uri, POST_FOLDER) { url ->
                if (url != null) {
                    binding.selectImage.setImageURI(uri)
                    imageUrl = url
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Mengatur toolbar dan tombol navigasi kembali
        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.materialToolbar.setNavigationOnClickListener {
            startActivity(Intent(this@PostActivity, HomeActivity::class.java))
            finish()
        }

        // Menggunakan launcher untuk memilih gambar dari penyimpanan perangkat
        binding.selectImage.setOnClickListener {
            launcher.launch("image/*")
        }

        // Tombol untuk membatalkan posting
        binding.cancelButton.setOnClickListener {
            startActivity(Intent(this@PostActivity, HomeActivity::class.java))
            finish()
        }

        // Tombol untuk memposting gambar
        binding.postButton.setOnClickListener {

            // Membuat objek Post dengan informasi gambar, keterangan, uid pengguna, dan waktu posting
            val post: Post = Post(
                postUrl = imageUrl!!,
                caption = binding.caption.editText?.text.toString(),
                uid = Firebase.auth.currentUser!!.uid,
                time = System.currentTimeMillis().toString()
            )

            // Menyimpan postingan ke koleksi POST dan juga di dokumen dengan UID pengguna
            Firebase.firestore.collection(POST).document().set(post).addOnSuccessListener {
                Firebase.firestore.collection(Firebase.auth.currentUser!!.uid).document()
                    .set(post)
                    .addOnSuccessListener {
                        startActivity(Intent(this@PostActivity, HomeActivity::class.java))
                        finish()
                    }
            }
        }
    }
}
```
- Deklarasi Variabel: Mendeklarasikan variabel binding untuk menggunakan View Binding dan imageUrl untuk menyimpan URL gambar yang akan diposting.
- ActivityResultLauncher: Membuat launcher menggunakan registerForActivityResult untuk mendapatkan konten gambar dari penyimpanan perangkat.
- Toolbar dan Tombol Navigasi: Mengatur toolbar dan tombol navigasi kembali.
- ClickListener untuk Memilih Gambar: Menggunakan launcher untuk memilih gambar dari penyimpanan perangkat.
- ClickListener untuk Membatalkan Posting: Memulai aktivitas HomeActivity jika tombol dibatalkan.
- ClickListener untuk Melakukan Posting: Membuat objek Post dan menyimpannya ke koleksi POST serta dokumen dengan UID pengguna.

# ReelsActivity
```
class ReelsActivity : AppCompatActivity() {

    // Inisialisasi variabel dengan menggunakan View Binding
    val binding by lazy {
        ActivityReelsBinding.inflate(layoutInflater)
    }

    private lateinit var videoUrl: String
    lateinit var progressDialog: ProgressDialog

    // ActivityResultLauncher untuk mendapatkan konten video dari penyimpanan perangkat
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            // Fungsi uploadVideo mengunggah video ke penyimpanan Firebase dan memberikan URL video
            uploadVideo(uri, REEL_FOLDER, progressDialog) { url ->
                if (url != null) {
                    videoUrl = url
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        progressDialog = ProgressDialog(this)

        // Memulai launcher untuk memilih video dari penyimpanan perangkat
        binding.selectReel.setOnClickListener {
            launcher.launch("video/*")
        }

        // Tombol untuk membatalkan posting reel
        binding.cancelButton.setOnClickListener {
            startActivity(Intent(this@ReelsActivity, HomeActivity::class.java))
            finish()
        }

        // Tombol untuk memposting reel
        binding.postButton.setOnClickListener {
            // Mendapatkan informasi pengguna dari Firestore
            Firebase.firestore.collection(USER_NODE).document(Firebase.auth.currentUser!!.uid)
                .get().addOnSuccessListener {

                    // Mengkonversi dokumen Firestore menjadi objek User
                    var user: User = it.toObject<User>()!!

                    // Membuat objek Reel dengan informasi URL video, keterangan, dan tautan profil pengguna
                    val reel: Reel = Reel(videoUrl!!, binding.caption.editText?.text.toString(), user.image!!)

                    // Menyimpan reel ke koleksi REEL dan juga di dokumen dengan UID pengguna
                    Firebase.firestore.collection(REEL).document().set(reel).addOnSuccessListener {
                        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + REEL).document()
                            .set(reel)
                            .addOnSuccessListener {
                                startActivity(Intent(this@ReelsActivity, HomeActivity::class.java))
                                finish()
                            }
                    }
                }
        }
    }
}
```
- Deklarasi Variabel: Menggunakan View Binding untuk inisialisasi variabel binding.
- Variabel Video URL: Mendeklarasikan variabel videoUrl untuk menyimpan URL video yang akan diposting.
- ProgressDialog: Membuat objek ProgressDialog untuk menampilkan dialog ketika proses upload video sedang berlangsung.
- ActivityResultLauncher: Membuat launcher menggunakan registerForActivityResult untuk mendapatkan konten video dari penyimpanan perangkat.
- OnClickListener untuk Memilih Video Reel: Memulai launcher untuk memilih video dari penyimpanan perangkat.
- OnClickListener untuk Membatalkan Posting Reel: Memulai aktivitas HomeActivity jika tombol dibatalkan.
- OnClickListener untuk Melakukan Posting Reel: Mendapatkan informasi pengguna dari Firestore, membuat objek Reel, dan menyimpannya ke koleksi REEL serta dokumen dengan UID pengguna.

# FILE UTILS
Pada bagian ini akan dijelaskan kode masing-masing class dari file utils

# Constant.kt
```
package com.projectmap.rizzler.utils

const val USER_NODE="User"
const val USER_PROFILE_FOLDER="Profile"
const val POST_FOLDER="PostImages"
const val REEL_FOLDER="Reels"
const val POST="Post"
const val REEL="Reel"
const val FOLLOW="FOLLOW"
```
File ini digunakan untuk menyimpan konstanta-konstanta yang digunakan di berbagai bagian aplikasi agar lebih mudah dikelola dan diubah jika diperlukan. Berikut adalah penjelasan dari setiap konstanta:
- USER_NODE: Konstanta ini menyimpan nama koleksi di Firebase Firestore yang digunakan untuk menyimpan data pengguna.
- USER_PROFILE_FOLDER: Konstanta ini menyimpan nama folder di penyimpanan Firebase Storage yang digunakan untuk menyimpan gambar profil pengguna.
- POST_FOLDER: Konstanta ini menyimpan nama folder di penyimpanan Firebase Storage yang digunakan untuk menyimpan gambar post.
- REEL_FOLDER: Konstanta ini menyimpan nama folder di penyimpanan Firebase Storage yang digunakan untuk menyimpan video reel.
- POST: Konstanta ini menyimpan nama koleksi di Firebase Firestore yang digunakan untuk menyimpan data post.
- REEL: Konstanta ini menyimpan nama koleksi di Firebase Firestore yang digunakan untuk menyimpan data reel.
- FOLLOW: Konstanta ini menyimpan nama koleksi di Firebase Firestore yang digunakan untuk menyimpan data pengguna yang di-follow oleh pengguna lain.
  
Dengan menggunakan file constant.kt dan menyimpan konstanta-konstanta ini di satu tempat, memudahkan pengelolaan dan pembaruan nilai konstanta ketika diperlukan, serta meningkatkan keterbacaan dan pemeliharaan kode.

# Utils.kt
File utils.kt tersebut berisi dua fungsi utilitas untuk mengunggah gambar (uploadImage) dan video (uploadVideo) ke Firebase Storage. 

# Upload Image Function
```
fun uploadImage(uri: Uri, folderName: String, callback: (String?) -> Unit) {
    var imageUrl: String? = null
    FirebaseStorage.getInstance().getReference(folderName).child(UUID.randomUUID().toString())
        .putFile(uri)
        .addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener {
                imageUrl = it.toString()
                callback(imageUrl)
            }
        }
}
```
- Fungsi ini menerima parameter uri yang merupakan URI gambar yang akan diunggah.
- folderName adalah nama folder di Firebase Storage tempat gambar akan diunggah.
- callback adalah fungsi yang akan dipanggil setelah proses pengunggahan selesai, dengan parameter imageUrl yang berisi URL gambar yang berhasil diunggah.
- UUID digunakan untuk memberikan nama unik untuk setiap file yang diunggah.
- Gambar diunggah ke Firebase Storage dan URL-nya diambil menggunakan downloadUrl.
- Hasil URL kemudian disampaikan ke dalam fungsi callback.

# Upload Video Function
```
fun uploadVideo(
    uri: Uri,
    folderName: String,
    progressDialog: ProgressDialog,
    callback: (String?) -> Unit
) {
    var imageUrl: String? = null
    progressDialog.setTitle("Uploading . . .")
    progressDialog.show()

    FirebaseStorage.getInstance().getReference(folderName).child(UUID.randomUUID().toString())
        .putFile(uri)
        .addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener {
                imageUrl = it.toString()
                progressDialog.dismiss()
                callback(imageUrl)
            }
        }
        .addOnProgressListener {
            val uploadedValue: Long = (it.bytesTransferred / it.totalByteCount)*100
            progressDialog.setMessage("Uploaded $uploadedValue %")
        }
}
```
- Fungsi ini mirip dengan uploadImage, namun menambahkan indikator kemajuan (progressDialog) untuk pengunggahan video.
- progressDialog digunakan untuk memberikan informasi tentang kemajuan pengunggahan.
- Pada addOnProgressListener, kemajuan pengunggahan dihitung dan pembaruan status ditampilkan di dalam progressDialog.
- Setelah selesai, progressDialog ditutup dan URL video disampaikan ke dalam fungsi callback.

Kedua fungsi ini memanfaatkan Firebase Storage untuk menyimpan gambar dan video, dan menggunakan UUID untuk memberikan nama file yang unik. Mereka juga menggunakan callback untuk memberi tahu pemanggil ketika proses pengunggahan selesai dengan sukses, dan dalam kasus uploadVideo, memberikan informasi kemajuan kepada pengguna melalui progressDialog.

# SELESAI DEHH
Sekian penjelasan singkat yang gak singkat singkat amat tentang kode dari project ini. Kami sangat berterimakasih tentu nya kepada ChatGPT, Tutor YT dari mas-mas India yang bisa dibilang tak terhitung banyak nya video yang kami tonton sampai berkali-kali rebuild project, dan terutama kepada pak Hudya yang berhasil mem push kita sejauh ini berkat pelajaran dari pak Hudya. Mohon maaf jika masih banyak kurang nya karena kami masih dalam tahap belajar. Terima Kasih sudah membaca explanation ini sampai habis:)








