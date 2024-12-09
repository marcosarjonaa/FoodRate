# Marcos Arjona Comino  
## PMyDM  
### IES Virgen del Carmen  

## FoodRate  
### Version 1.2  

**Marcos Arjona Comino**  
**Enlace a GitHub:**  
[FoodRate Repository](https://github.com/marcosarjonaa/FoodRate)  

**Programación Multimedia y dispositivos Móviles**  
2ºDAM A  

**Multimedia y dispositivos móviles**

---

### MainActivity:  
En el código que se ve a continuación, podemos observar:  
1. **Creación del binding y del fichero compartido.**  
2. En el `onCreate`, se inicializa la variable `binding` y se llama a las funciones `initSharedPreferences` y `mostrarUsuCon`.  

#### Métodos destacados:  
- **mostrarUsuCon:** Recupera las variables que previamente han sido enviadas al inicio del `MainActivity` mediante `putExtras`.  
- **initSharedPreferences:** Crea y gestiona las preferencias compartidas.  

---

### Activity Main  
En esta activity se realiza:  
- La creación de **CardViews**, a los cuales se les añade un **FrameLayout** para estructurarlos.  
- Su diseño incluye:
  - Dos párrafos de texto.
  - Una imagen que aporta contexto al contenido.  

#### Nota:  
Más allá de esto, no tiene nada especial que no haya hecho anteriormente.

---

### LoginActivity  
En este archivo encontramos:  
- Un inicio de programa en el `onCreate`.  
- Un **binding** que detecta cuando el botón de **"Entrar"** es presionado.  
  - Recupera el texto de los `EditText` para **usuario** y **contraseña**.  
  - Comprueba que los valores sean correctos.  
  - En caso contrario, imprime un mensaje de error.  

#### Activity Login:  
Su diseño incluye:  
- **EditText** para usuario y contraseña.  
- Una imagen decorativa.  
- Uso de **ConstraintLayout** para evitar desplazamientos de los elementos.  

#### Nota:  
No tiene características especiales adicionales.

---

# Explicación del Código

## **1. AdapterRecetas: Adaptador para el RecyclerView**
Este adaptador maneja una lista de recetas y controla cómo se muestran en el RecyclerView.

### **Propiedades:**
- **listRecetas:** Lista mutable de recetas.
- **deleteClick:** Función lambda que se ejecuta al eliminar una receta.

### **Métodos:**
- **onCreateViewHolder:**  
  Crea una instancia de `ViewHRecetas` inflando el layout `activity_cardview`.
  
- **onBindViewHolder:**  
  Asocia los datos de una receta específica (de `listRecetas[position]`) a su respectivo `ViewHolder`.

- **getItemCount:**  
  Devuelve el tamaño de la lista.

---

## **2. ViewHRecetas: ViewHolder personalizado**
Se encarga de vincular los datos de una receta a las vistas correspondientes del layout.

### **Propiedades:**
- **binding:** Utiliza `ActivityCardviewBinding` para manejar las vistas.
- **deleteOnClick:** Lambda que define qué sucede al eliminar una receta.

### **Métodos:**
- **renderize:**  
  Muestra los datos de una receta (de la clase `Recetas`) en las vistas:
  - Usa **Glide** para cargar imágenes de manera eficiente.
  - Configura un listener para eliminar recetas (mediante `setOnClickListener`).

- **setOnClickListener:**  
  Establece el comportamiento al pulsar el botón de eliminar (`ivDelete`) o editar (`ivEdit`).

---

## **3. Controller: Controlador principal**
Maneja la lógica de datos y la configuración del RecyclerView.

### **Propiedades:**
- **listRecetas:** Lista mutable de recetas, inicializada desde el DAO.
- **adapter:** Instancia del adaptador `AdapterRecetas`.
- **layoutManager:** Define cómo se organiza el `RecyclerView` (en este caso, en una disposición lineal).

### **Métodos:**
- **initdata:**  
  Carga las recetas iniciales desde `DaoRecetas`.

- **logOut:**  
  Muestra un mensaje y los datos de las recetas en consola.

- **setAdapter:**  
  Configura el adaptador y el `RecyclerView` en `MainActivity`.

- **deleteRecetas:**  
  Elimina una receta de la lista si el índice es válido:
  - Actualiza el `RecyclerView` mediante `notifyItemRemoved` y `notifyItemRangeChanged`.
  - Muestra un mensaje indicando el éxito o error de la operación.

---

## **4. DaoRecetas: Acceso a los datos**
Singleton que implementa `InterfaceDao` para gestionar la lista de recetas desde un repositorio.

### **Métodos:**
- **getDataRecetas:**  
  Devuelve la lista estática `ListaRecetas` desde `Repositorio`.

---

## **5. InterfaceDao: Contrato para acceder a los datos**
Define un método:
- **getDataRecetas:** Devuelve una lista de recetas.

---

## **6. Recetas: Modelo de datos**
Representa una receta con las siguientes propiedades:
- **name:** Nombre de la receta.
- **descripcion:** Descripción de la receta.
- **nota:** Notas o calificación de la receta.
- **image:** URL de la imagen asociada.

### **Sobreescritura:**
- **toString:**  
  Devuelve una representación textual de la receta, útil para depuración.

---

## **Flujo General:**
1. El controlador (`Controller`) inicializa los datos con `DaoRecetas` y configura el adaptador.
2. El adaptador (`AdapterRecetas`) vincula la lista de recetas con el `RecyclerView`.
3. Los `ViewHRecetas` renderizan los datos de cada receta y manejan las interacciones.
4. El usuario puede eliminar recetas mediante `deleteRecetas`, lo que actualiza la lista y el adaptador.

---
