# Marcos Arjona Comino  
## PMyDM  
### IES Virgen del Carmen  
---

# FoodRate  

**Programación Multimedia y dispositivos móviles**
**2ºDAM A**

---

### MainActivity:  
#### Métodos:  
- **initSharedPreferences:** Gestiona las preferencias compartidas.
- **onCreate:** Crea e infla el activity a través del binding Main.
- **init:** Setea las variable controller.

#### Código
``` kotlin
    lateinit var controller: Controller
    lateinit var bindingMain : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindingMain = ActivityMainBinding.inflate(layoutInflater);
        setContentView(bindingMain.root);
        init()
    }

    private fun init(){
        initRecyclerView()
        controller = Controller(this)
        controller.setAdapter()
    }

    private fun initRecyclerView() {
        bindingMain.myRecyclerView.layoutManager = LinearLayoutManager(this)
    }
```

---

### LoginActivity  
#### Métodos: 
- **onCreate** lo mismo que en el código anterior
- **init:** inicializa todos los botones
- **start:** recoge todos los para comprobar los campos de correo y contraseña  para iniciar sesion
- **recoverPassword:** manda el email para recuperar la contraseña-
- **startlogin:** compruba definitivamente la conexion a firebase
- **salvarLogueo:** comprueba si hay datos guardados para cuando el login se queda guardado
- **estaLogueado:** comprueba si está logueado
- **navigateToMainActivity:** navega al main activity 

#### Código: 
``` kotlin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()
        if (estaLogueado()) {
            navigateToMainActivity()
            finish()
        }
        init()
        start()
    }

    private fun init() {
        btnLogin = findViewById(R.id.btn_login_in_login)
        btnRegister = findViewById(R.id.btn_register_in_login)
        btnRecoverPass = findViewById(R.id.btn_recover_pass)
        editUser = findViewById(R.id.edit_user_login)
        editPassword = findViewById(R.id.edit_pass_login)
    }

    private fun start() {
        btnLogin.setOnClickListener {
            val user = editUser.text.toString()
            val pass = editPassword.text.toString()

            if (user.isNotEmpty() && pass.isNotEmpty())
                startLogin(user, pass) { result, msg ->
                    Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_LONG).show()
                    if (result) {
                        salvarLogueo(user) // Save login state
                        navigateToMainActivity()
                        finish()
                    }
                }
            else
                Toast.makeText(this, "Tienes algún campo vacío", Toast.LENGTH_LONG).show()
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this, RegistrarActivity::class.java)
            startActivity(intent)
        }

        btnRecoverPass.setOnClickListener {
            val user = editUser.text.toString()
            if (user.isNotEmpty())
                recoverPassword(user) { result, msg ->
                    Toast.makeText(this@LoginActivity, msg, Toast.LENGTH_LONG).show()
                    if (!result) editUser.setText("")
                }
            else
                Toast.makeText(this, "Debes rellenar el campo email", Toast.LENGTH_LONG).show()
        }
    }

    private fun recoverPassword(email: String, onResult: (Boolean, String) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { taskResetEmail ->
                if (taskResetEmail.isSuccessful) {
                    onResult(true, "Acabamos de enviarte un email con la nueva password")
                } else {
                    val msg = try {
                        throw taskResetEmail.exception ?: Exception("Error de reseteo inesperado")
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        "El formato del email es incorrecto"
                    } catch (e: Exception) {
                        e.message.toString()
                    }
                    onResult(false, msg)
                }
            }
    }

    private fun startLogin(user: String, pass: String, onResult: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(user, pass)
            .addOnCompleteListener { taskAssin ->
                if (taskAssin.isSuccessful) {
                    val posibleUser = auth.currentUser
                    if (posibleUser?.isEmailVerified == true) {
                        onResult(true, "Usuario Logueado satisfactoriamente")
                    } else {
                        auth.signOut()
                        onResult(false, "Debes verificar tu correo antes de loguearte")
                    }
                } else {
                    val msg = try {
                        throw taskAssin.exception ?: Exception("Error desconocido")
                    } catch (e: FirebaseAuthInvalidUserException) {
                        "El usuario tiene problemas por haberse borrado o desabilitado"
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        if (e.message?.contains("There is no user record corresponding to this identifier") == true) {
                            "El usuario no existe"
                        } else "Contraseña incorrecta"
                    } catch (e: Exception) {
                        e.message.toString()
                    }
                    onResult(false, msg)
                }
            }
    }

    private fun salvarLogueo(email: String) {
        val sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user_email", email)
        editor.putBoolean("is_logged_in", true)
        editor.apply()
    }

    private fun estaLogueado(): Boolean {
        val sharedPreferences = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
        return sharedPreferences.getBoolean("is_logged_in", false)
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
```

---
### RegistrarActivty
#### Métodos
- **onCreate.**
- **init.**
- **start:** comprueba que los valores sean distintos y sino, los registra
- **startActivityLogin:** te manda al activity de login
- **registerUser:** crea el usuario y lanza excepciones en caso que sean necesario

#### Código
``` kotlin
override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()
        start()
    }

    private fun init() {
        btnRegister = findViewById(R.id.btn_register_in_register)
        btnLastRegister = findViewById(R.id.btn_last_register)
        editUser = findViewById(R.id.edit_user_register)
        editPassword = findViewById(R.id.edit_pass_register)
        editRepeatPassword = findViewById(R.id.pass_register_repeat_in_register)

        auth = Firebase.auth  //Creamos nuestro objeto de autenticación

    }


    private fun start() {
        btnRegister.setOnClickListener {
            val email = editUser.text.toString()
            val pass = editPassword.text.toString()
            val repeatPass = editRepeatPassword.text.toString()
            if (pass != repeatPass
                || email.isEmpty()
                || pass.isEmpty()
                || repeatPass.isEmpty()
            )
                Toast.makeText(this, "Campos vacíos y/o password diferentes", Toast.LENGTH_LONG)
                    .show()
            else {
                registerUser(email, pass) { result, msg ->
                    Toast.makeText(this@RegistrarActivity, msg, Toast.LENGTH_LONG).show()
                    if (result)
                        startActivityLogin()
                }
            }
        }

        btnLastRegister.setOnClickListener {
            // Como la lambda tiene un parámetro view, el this es el Activity
                view ->
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun startActivityLogin() {
        //Tengo que lanzar un intent con el Activity a loguear.
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() //No quiero que sigua el Activity del registro.

    }

    private fun registerUser(email: String, pass: String, onResult: (Boolean, String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { taskAssin ->
                if (taskAssin.isSuccessful) {
                    //enviaremos un email de confirmación
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { taskVerification ->
                            var msg = ""
                            if (taskVerification.isSuccessful)
                                msg = "Usuario Registrado Ok. Verifique su correo"
                            else
                                msg =
                                    "Usuario Registrado Ok. ${taskVerification.exception?.message}"
                            auth.signOut() //tiene que verificar antes el email
                            onResult(true, msg)
                        }
                        ?.addOnFailureListener { exception ->
                            Log.e(
                                "ActivityRegister",
                                "Fallo al enviar correo de verificación: ${exception.message}"
                            )
                            onResult(
                                false,
                                "No se pudo enviar el correo de verificación: ${exception.message}"
                            )
                        }

                } else {
                    try {
                        throw taskAssin.exception ?: Exception("Error desconocido")
                    } catch (e: FirebaseAuthUserCollisionException) {
                        onResult(false, "Ese usuario ya existe")
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        onResult(false, "La contraseña es débil: ${e.reason}")
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        onResult(false, "El email proporcionado, no es válido")
                    } catch (e: Exception) {
                        onResult(false, e.message.toString())
                    }

                }
            }


    }
```

---
## Carpeta Adapter
- ##### **AdapterRecetas**
- ##### **ViewHRecetas**

  
### AdapterRecetas
#### Métodos
- **onCreate.**
- **onBindViewHolder:** renderiza el viewholder
- **getItemCount:** recibe el tamaño de la lista de recetas

#### Código
```kotlin
class AdapterRecetas(
    var listRecetas: MutableList<Recetas>,
    var deleteClick: (Int) -> Unit,
    var updateClick: (Int) -> Unit
    ) : RecyclerView.Adapter<ViewHRecetas>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHRecetas {
            val layoutInflater = LayoutInflater.from(parent.context)
            val layoutItemRecetas = R.layout.activity_cardview
            return ViewHRecetas(layoutInflater.inflate(layoutItemRecetas, parent, false), deleteClick, updateClick)
        }

        override fun onBindViewHolder(holder: ViewHRecetas, position: Int) {
            holder.renderize(listRecetas[position], position)
        }

        override fun getItemCount(): Int = listRecetas.size


}
```

---
### ViewHRecetas
#### Métodos
- **init:** iniciar el binding
- **renderize:** renderiza los textos de las recetas.
- **setOnClickListener:** escucha para ver si han clicado  en delete  o update. 

#### Código
``` kotlin
class ViewHRecetas (
    view: View,
    var deleteOnClick: (Int) -> Unit,
    var updateOnClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder (view){
    var binding: ActivityCardviewBinding

    init {
        binding = ActivityCardviewBinding.bind(view)
    }

    fun renderize(recetas: Recetas, position: Int){
        binding.txtNombre.setText(recetas.name)
        binding.txtDescripcion.setText(recetas.descripcion)
        binding.txtNota.setText(recetas.nota)
        Glide
            .with(itemView.context)
            .load(recetas.image)
            .centerCrop()
            .into(binding.ivRecetas)
        setOnClickListener(position)
    }

    private fun setOnClickListener(position : Int) {
        binding.ivDelete.setOnClickListener {
            deleteOnClick(position)
        }
        binding.ivEdit.setOnClickListener {
            updateOnClick(position)
        }
    }
}
```

---
## Carpeta Controller
- ##### Controller

### Controller
#### Métodos
- **initdata**
- **logOut**
- **setAdapter:** carga el adapter con métodos posteriormente explicados
  - **deleteRecetas:** método que se manda al adapter encargado de eliminar una receta segun su posición
  - **updateRecetas:** método que se manda al adapter encargado de lanzar el dialogo de editar
- **initOnClickListener:** carga el contexto del MainActivity
- **okOnEditReceta:** confirma el cambio de datos, eliminando el item que habia en esa posición y cambiandolo por el nuevo
- **addReceta:** añade una receta lanzando su correspondiente dialog
- **okOnNewReceta:** confirma el añadido de datos que necesita el item 

#### Código:
``` kotlin
class Controller (val context: Context) {
    lateinit var listRecetas : MutableList<Recetas>
    private lateinit var adapter: AdapterRecetas
    private var layoutManager: LinearLayoutManager = LinearLayoutManager(context)

    init {
        initdata()
        setAdapter()
        logOut()
        initOnClickListener()
    }

    fun initdata(){
        listRecetas = DaoRecetas.myDao.getDataRecetas().toMutableList()
    }

    fun logOut(){
        Toast.makeText(context, "He mostrado los datos", Toast.LENGTH_LONG).show()
        listRecetas.forEach{
            println(it)
        }
    }

    fun setAdapter(){
        val myActivity = context as MainActivity
        adapter = AdapterRecetas(
            listRecetas,
            { pos -> deleteRecetas(pos) },
            { pos -> updateRecetas(pos) }
        )
        myActivity.bindingMain.myRecyclerView.layoutManager = layoutManager
        myActivity.bindingMain.myRecyclerView.adapter = adapter
    }

    fun deleteRecetas(posicion : Int){
        val myActivity = context as MainActivity
        val dialogDelete = DialogDeleteRecetas(posicion){
            if(posicion in listRecetas.indices){
                listRecetas.removeAt(posicion)
                myActivity.bindingMain.myRecyclerView.adapter?.apply{
                    notifyItemRemoved(posicion)
                    notifyItemRangeChanged(posicion, listRecetas.size - posicion)
                }
                Toast.makeText(context, "Se eliminó el monumento en la posicion $posicion", Toast.LENGTH_LONG).show()
            }else {
                Toast.makeText(context, "Índice fuera de ranfo: $posicion", Toast.LENGTH_LONG).show()
            }
        }
        dialogDelete.show(myActivity.supportFragmentManager, "Borro una receta")
    }

    private fun initOnClickListener() {
        val myActivity = context as MainActivity;
        myActivity.bindingMain.btnAdd.setOnClickListener{
            addReceta()
        }
    }

    fun updateRecetas(pos: Int) {
        val editDialog = DialogEditRecetas(listRecetas.get(pos)){
            editReceta -> okOnEditReceta(editReceta, pos)
        }
        val myActivity = context as MainActivity
        editDialog.show(myActivity.supportFragmentManager, "Edito una receta")
    }

    private fun okOnEditReceta(editRecetas: Recetas, pos: Int){
        listRecetas.removeAt(pos);
        adapter.notifyItemRemoved(pos);
        listRecetas.add(pos, editRecetas);
        adapter.notifyItemInserted(pos);

        val myActivity = context as MainActivity;
        myActivity.bindingMain.myRecyclerView.post {
            layoutManager.scrollToPositionWithOffset(pos, 20);
        }
    }

    fun addReceta() {
        Toast.makeText(context, "Añadimos una receta", Toast.LENGTH_LONG).show()
        val dialog = DialogAddRecetas { receta -> okOnNewReceta(receta)}
        val myActivity = context as MainActivity;
        dialog.show(myActivity.supportFragmentManager, "Añado una nueva receta");
    }

    private fun okOnNewReceta(receta: Recetas){
        Log.d("Controler", "Añadiendo receta: $receta");
        listRecetas.add(listRecetas.size, receta);
        adapter.notifyItemInserted(listRecetas.lastIndex)
        val myActivity = context as MainActivity;
        myActivity.bindingMain.myRecyclerView.post{
            layoutManager.scrollToPositionWithOffset(listRecetas.size, 25)
        }
    }
}
```
---

## Carpeta Interfaces
- ##### InterfaceDao


### InterfaceDao
#### Métodos
- **getDataRecetas**

#### Código
``` kotlin
interface InterfaceDao {
    fun getDataRecetas(): List<Recetas>
}
```
---

## Carpeta DAO
- ##### DaoRecetas

### DaoRecetas
#### Métodos
- **companion object:** uso del byLazy
- **getDataRecetas:** Lanza el repositorio de Lista de recetas

#### Código
``` kotlin
class DaoRecetas private constructor() : InterfaceDao {
    companion object{
        val myDao: DaoRecetas by lazy {
            DaoRecetas()
        }
    }

    override
    fun getDataRecetas(): List<Recetas>  = Repositorio.ListaRecetas
}
```
---

## Carpeta Models
- ##### Recetas

### Recetas
#### Métodos
- **toString:** convierte a string los parámetros necesarios de name, descripcion, nota e image.

#### Códgo: 
``` kotlin
class Recetas(
    var name: String,
    var descripcion: String,
    var nota: String,
    var image: String
) {
    override fun toString(): String {
        return "Recetas(name='$name', descripcion'$descripcion',nota='$nota', image='$image')"
    }
}
```
--- 
## Carpeta Object_Models
- ##### ArgumentsRecetas
- ##### Recetas

###  ArgumentsRecetas
#### Código
``` kotlin
object ArgumentsRecetas {
    const val ARGUMENT_NAME = "name"
    const val ARGUMENT_DESCRIPCION = "descripcion"
    const val ARGUMENT_NOTA = "nota"
    const val ARGUMENT_IMAGE = "image"
}
```
---
### Repositorio
#### Código
``` kotlin
object Repositorio {
    val ListaRecetas : List<Recetas> = listOf(
        Recetas(
            "Bolos lucentinos",
            "Los bolos lucentinos son una comida que ha pasado generación tras generacion" +
                    " de abuelas a madres. Típica de Lucena, es un plato que une la carne y las almendras.",
            "8",
            "https://cocinandoentreolivos.com/wp-content/uploads/2017/11/Bolos-Lucentinos-tradicionales-Lucena-www.cocinandoentreolivos.com-30.jpg"
        ),
        Recetas(
            "Tirabuzones",
            "Los tirabuzones son una comida típica de la semana santa, sobretodo en lucena donde cada vez es menos visto," +
                    "haciendo que esta receta se encuentre más perdida.",
            "7",
            "https://www.2mandarinasenmicocina.com/wp-content/uploads/2017/12/Tirabuzones-de-El-Hornillo-Avila-web.jpg"
        ),
        Recetas(
            "Flamenquín",
            "Los flamenquines son una comida típica cordobesa que se ha extendido a toda españa." +
                    " Aunque la receta original es con lomo y jamón se puede...",
            "9",
            "https://4.bp.blogspot.com/-M1tvwbGwXdc/WN4CDjc3hbI/AAAAAAAAOi4/8q2rSjYzrGgzrLXOy8enRIvNxsZ75FaOwCLcB/s1600/flacor.jpg"
        ),
        Recetas(
            "Salmorejo",
            "El salmorejo es una comida típica cordobesa cuya base es una mezcla de tomate con otras verduras" +
                    ", tambien se le suele echar huevo duro y jamón",
            "7",
            "https://www.196flavors.com/wp-content/uploads/2021/09/Salmorejo-2fp.jpg"
        ),
        Recetas(
            "Ochios",
            "Los ochios son un plato común de la provincia de Jaén, originario de Úbeda y Baeza. " +
                    "Se suele comer rellenos de morcilla o pavo en su mayoria, pero tambien hay variantes dulces.",
            "8",
            "https://www.vandelviraturismo.com/wp-content/uploads/2020/09/ochios-en-baeza-comida-tipica-768x576.jpg"
        ),
        Recetas(
            "Spaghetti a la ...",
            "Receta italiana que se ha expandido por todo el mundo, sus ingredientes consisten en spaghetti, guanciale, peccorino, pimienta negra" +
                    ". A la carbonara, viene por dos teorías , porque...",
            "9",
            "https://buenprovecho.hn/wp-content/uploads/2020/10/Espaguetis_a_la_Carbonara.jpg"
        ),
        Recetas(
            "Pizza margharitta",
            "La pizza margharitta viene por la visita de la reina italiana Margarita a napoles, donde buscando honorificar la " +
                    "bandera italiana , buscaron la forma de hacer una pizza con los colores de la bandera.",
            "10",
            "https://www.annarecetasfaciles.com/files/pizza-margarita-1-scaled.jpg"
        ),
        Recetas(
            "Tortilla de patatas",
            "La tortilla de patatas, es una receta con origen español que mezcla las patatas con el huevo, aunque existe el debate" +
                    "de si va acompañada de cebolla o no...",
            "10",
            "https://www.romagnolipatate.it/images/shutterstock_1669555969-2-2-mi.jpg"
        ),
        Recetas(
            "Patatas bravas",
            "Las patatas bravas tienen su origen en Madrid, no es certero donde se vendieron las primeras. Es una tapa " +
                    "típicas de España. Donde , sobre todo, en bares se suelen comer",
            "7",
            "https://www.lieferando.de/foodwiki/uploads/sites/8/2018/01/patatas-bravas-2-1080x960.jpg"
        ),
        Recetas(
            "Rabo de toro",
            "El rabo de toro es una comida tipica cordobesa aunque cada vez mas extendida por España. Esta comida suele servirse sobre un caldo de verduras" +
                    " donde resalta la zanahoria y con un parmentier de  patata",
            "7",
            "https://cdn.tasteatlas.com/images/dishes/5d1064ee4af349a4b9a72993acc6e993.jpg"
        ),
    )
}
```
--- 

## Carpeta Dialogues
- ##### DialogAddRecetas
- ##### DialogEditRecetas
- ##### DialogDeletaRecetas

### DialogAddRecetas
#### Métodos
- **onCreateDialog:** devuelve el activity donde se meten los datos necesarios para crear una nueva recetas
- **validacion:** comprueba que los campos del dialog no sean empty
- **recoverDataLayout:** carga el binding para recoger datos 

#### Código
``` kotlin
class DialogAddRecetas (
    private val onNewRecetasDialog: (Recetas) -> Unit
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { myActivity ->
            val builder = AlertDialog.Builder(myActivity);
            val inflater = myActivity.layoutInflater;
            val viewDialog = inflater.inflate(R.layout.dialog_add_recetas, null)
            builder.setView(viewDialog)
            builder.setMessage("Añadir una receta")
                .setPositiveButton("Crear",
                    DialogInterface.OnClickListener{
                        dialog, id ->
                        val receta = recoverDataLayout(viewDialog)
                        if(validacion(receta)){
                            onNewRecetasDialog(receta);
                            Toast.makeText(myActivity, "receta creada", Toast.LENGTH_LONG).show()
                        }else {
                            Toast.makeText(myActivity, "Llena todos los campos", Toast.LENGTH_LONG).show()
                        }
                    }
                ).setNegativeButton("Cancelar",
                    DialogInterface.OnClickListener{dialog, id ->
                        Toast.makeText(myActivity, "Has cerrado la ventana de añadir", Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    }
                    ).create()
        } ?: throw IllegalStateException("Activity cannot be null")
        requireActivity().supportFragmentManager.beginTransaction().detach(this).attach(this).commit()
    }

    private fun validacion(receta: Recetas): Boolean {
        return receta.name.isNotEmpty() &&
                receta.descripcion.isNotEmpty() &&
                receta.nota.isNotEmpty() &&
                receta.image.isNotEmpty()
    }

    private fun recoverDataLayout(view: View): Recetas {
        val binding = DialogAddRecetasBinding.bind(view)
        return Recetas(
            binding.etName.text.toString(),
            binding.etDescripcion.text.toString(),
            binding.etNota.text.toString(),
            binding.etImage.text.toString()
        )
    }
}
```
---
### DialogEditRecetas
#### Métodos
- **init:** recupera los valores de strings
- **onCreateDialog:** devuelve el activity donde se meten los datos necesarios para editar la receta cargando los datos
- **recoverDataLayout:** carga el binding para recoger datos 

#### Código
``` kotlin
class DialogEditRecetas (
    val recetasToUpdate: Recetas,
    val updateRecetasDialog: (Recetas) -> Unit
) : DialogFragment() {
    private val ARGUMENT_NAME : String = ArgumentsRecetas.ARGUMENT_NAME
    private val ARGUMENT_DESCRIPCION : String = ArgumentsRecetas.ARGUMENT_DESCRIPCION
    private val ARGUMENT_NOTA : String = ArgumentsRecetas.ARGUMENT_NOTA
    private val ARGUMENT_IMAGE : String = ArgumentsRecetas.ARGUMENT_IMAGE
    init{
        val args = Bundle().apply {
            putString(ARGUMENT_NAME, recetasToUpdate.name)
            putString(ARGUMENT_DESCRIPCION, recetasToUpdate.descripcion)
            putString(ARGUMENT_NOTA, recetasToUpdate.nota)
            putString(ARGUMENT_IMAGE, recetasToUpdate.image)
        }
        this.arguments = args
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let{
            val inflater = requireActivity().layoutInflater
            val binding = DialogEditRecetasBinding.inflate(inflater)
            arguments?.let { args ->  // seteo los datos iniciales en los campos del dialogo
                binding.etName.setText(args.getString(ARGUMENT_NAME))
                binding.etdescripcion.setText(args.getString(ARGUMENT_DESCRIPCION))
                binding.etNota.setText(args.getString(ARGUMENT_NOTA))
                binding.etImage.setText(args.getString(ARGUMENT_IMAGE))
            }
            val builder = AlertDialog.Builder(requireContext())
            builder.setView(binding.root)
                .setTitle("Editar monumento: ")
                .setPositiveButton("Aceptar") { dialog, id ->
                    val updateRecetas = recoverDataLayout(binding)

                    if (updateRecetas.name.isEmpty() ||
                        updateRecetas.descripcion.isEmpty() ||
                        updateRecetas.nota.isEmpty() ||
                        updateRecetas.image.isEmpty()
                    ) {
                        Toast.makeText(requireContext(), "Algún campo está vacío", Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    } else {
                        updateRecetasDialog(updateRecetas)

                    }
                }
                .setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.cancel()
                }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
        requireActivity().supportFragmentManager.beginTransaction().detach(this).attach(this).commit()
    }

    private fun recoverDataLayout(binding: DialogEditRecetasBinding): Recetas {
        return Recetas(
            binding.etName.text.toString(),
            binding.etNota.text.toString(),
            binding.etdescripcion.text.toString(),
            binding.etImage.text.toString()
        )
    }
}
```
---
### DialogDeleteRecetas
#### Métodos
- **init:** recupera los valores de strings
- **onCreateDialog:** devuelve el activity donde se pide la confirmacion de la voluntad de eliminar la receta

#### Código
``` kotlin
class DialogDeleteRecetas (
    val pos : Int,
    val onDeleteRecetasDialog: (Int) -> Unit
) : DialogFragment(){
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { myActivity ->
            val builder = AlertDialog.Builder(myActivity)
            val inflater = myActivity.layoutInflater
            val viewDialog = inflater.inflate(R.layout.dialog_delete_recetas, null)
            builder.setView(viewDialog)
            builder.setMessage("¿Quieres eliminar la receta?")
                .setPositiveButton("Eliminar") { dialog, id ->
                    onDeleteRecetasDialog(pos)
                }
                .setNegativeButton("Cancelar") { dialog, id ->
                    dialog.cancel()
                }
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
        requireActivity().supportFragmentManager.beginTransaction().detach(this).attach(this).commit()
    }
}
```
--- 



# Hecho por Marcos Arjona Comino


