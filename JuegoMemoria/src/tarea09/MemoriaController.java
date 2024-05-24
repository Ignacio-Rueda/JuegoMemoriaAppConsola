package tarea09;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Clase Controladora
 *
 * @author INDICAR NOMBRE DEL ALUMNO
 */
public class MemoriaController implements Initializable {

    // definición de variables internas para el desarrollo del juego
    private JuegoMemoria juego;         // instancia que controlará el estado del juego (tablero, parejas descubiertas, etc)
    private ArrayList<Button> cartas;   // array para almacenar referencias a las cartas @FXML definidas en la interfaz 
    private int segundos = 0;             // tiempo de juego
    private boolean primerBotonPulsado = false, segundoBotonPulsado = false; // indica si se han pulsado ya los dos botones para mostrar la pareja
    private int idBoton1, idBoton2;     // identificadores de los botones pulsados
    private boolean esPareja;           // almacenará si un par de botones pulsados descubren una pareja o no
    boolean permitePulsar = true;       //Dentro del espacio de tiempo habilitado no debemos permitir que se pulsen mas de dos botones de manera consecutiva, hasta que no se valide si estos son pareja o no lo son.
    boolean esFinalPartida;

    private Time time;
    private final String rutaMusicaFondo = System.getProperty("user.dir") + File.separator + "src" + File.separator + "tarea09" + File.separator + "assets" + File.separator + "sonidos" + File.separator + "musica.mp3";
    private final String rutaSonidoPareja = System.getProperty("user.dir") + File.separator + "src" + File.separator + "tarea09" + File.separator + "assets" + File.separator + "sonidos" + File.separator + "pareja.mp3";
    private final String rutaSonidoNoPareja = System.getProperty("user.dir") + File.separator + "src" + File.separator + "tarea09" + File.separator + "assets" + File.separator + "sonidos" + File.separator + "noPareja.mp3";
    private final String rutaSonidoVictoria = System.getProperty("user.dir") + File.separator + "src" + File.separator + "tarea09" + File.separator + "assets" + File.separator + "sonidos" + File.separator + "tada.mp3";
    private final String rutaSonidoBye = System.getProperty("user.dir") + File.separator + "src" + File.separator + "tarea09" + File.separator + "assets" + File.separator + "sonidos" + File.separator + "bye_bye.mp3";
    private final String rutaIconoSalir = System.getProperty("user.dir") + File.separator + "src" + File.separator + "tarea09" + File.separator + "assets" + File.separator + "interfaz" + File.separator + "iconoSalir.png";

    private String tipoJuego;
    private String rutaImagenCarta;

    private final File fileMusicaFondo = new File(rutaMusicaFondo);
    private final Media mediaMusicaFondo = new Media(fileMusicaFondo.toURI().toString());
    private MediaPlayer mediaPlayerMusicaFondo;

    private final File fileNoEsPareja = new File(rutaSonidoNoPareja);
    private final Media mediaNoEsPareja = new Media(fileNoEsPareja.toURI().toString());
    private MediaPlayer mediaPlayerNoEsPareja;

    private final File fileEsPareja = new File(rutaSonidoPareja);
    private final Media mediaEsPareja = new Media(fileEsPareja.toURI().toString());
    private MediaPlayer mediaPlayerEsPareja;

    private final File fileBye = new File(rutaSonidoBye);
    private final Media mediaBye = new Media(fileBye.toURI().toString());
    private MediaPlayer mediaPlayerBye;

    private final File fileVictoria = new File(rutaSonidoVictoria);
    private final Media mediaVictoria = new Media(fileVictoria.toURI().toString());
    private MediaPlayer mediaPlayerVictoria;

    private int contadorIntentos = 0;
    @FXML
    private GridPane card;   //Carta
    @FXML
    private AnchorPane main;      // panel principal (incluye la notación @FXML porque hace referencia a un elemento de la interfaz)

    @FXML
    private Label estadoTiempo, numIntentos, etiquetaTipoJuego;
    //Referencia a las cartas
    @FXML
    private Button carta1, carta2, carta3, carta4, carta5, carta6, carta7, carta8, carta9, carta10, carta11, carta12, carta13, carta14, carta15, carta16;

    // linea de tiempo para gestionar la finalización del intento al pasar 1.5 segundos
    private final Timeline finIntento = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> finalizarIntento()));
    // Línea de tiempo para dar tiempo a reproducir el sonido de despedida de juego.
    private final Timeline despedida = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> Platform.exit()));
    

    // linea de tiempo para gestionar el contador de tiempo del juego
    private Timeline contadorTiempo;

    /**
     * Método interno que configura todos los aspectos necesarios para
     * inicializar el juego.
     *
     * @param url No utilizaremos este parámetro (null).
     * @param resourceBundle No utilizaremos este parámetro (null).
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        juego = new JuegoMemoria(); // instanciación del juego (esta instancia gestionará el estado de juego)
        juego.iniciarJuego();       // comienzo de una nueva partida
        cartas = new ArrayList<>(); // inicialización del ArrayList de referencias a cartas @FXML
        tipoJuego = juego.getTipoPartida();
        rutaImagenCarta = "assets" + File.separator + "cartas" + File.separator + tipoJuego + File.separator;
        etiquetaTipoJuego.setText("MEMORIZA: " + tipoJuego.toUpperCase() + "!");

        // guarda en el ArrayList "cartas" todas las referencias @FXML a las cartas para gestionarlo cómodamente
        cartas.add(carta1);
        cartas.add(carta2);
        cartas.add(carta3);
        cartas.add(carta4);
        cartas.add(carta5);
        cartas.add(carta6);
        cartas.add(carta7);
        cartas.add(carta8);
        cartas.add(carta9);
        cartas.add(carta10);
        cartas.add(carta11);
        cartas.add(carta12);
        cartas.add(carta13);
        cartas.add(carta14);
        cartas.add(carta15);
        cartas.add(carta16);

        // inicialización de todos los aspectos necesarios
        for (Button boton : cartas) {
            boton.getStyleClass().add("fondo-transparente");
            boton.setDisable(false);//Inicialmente todos los botones podrán ser pulsados
        }
        card.getStyleClass().remove("gridPaneFinalPartida");
        numIntentos.setText("0");
        primerBotonPulsado = false;
        segundoBotonPulsado = false;
        contadorIntentos = 0;  //Inicializar contador de intentos
        mediaPlayerMusicaFondo = new MediaPlayer(mediaMusicaFondo);
        // contador de tiempo de la partida (Duration indica cada cuanto tiempo se actualizará)
        time = new Time("0:0:0");
        contadorTiempo = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            /// acciones a realizar (este código se ejecutará cada segundo)
            time.oneSecondPassed();
            estadoTiempo.setText(time.getCurrentTime());
        }));
        contadorTiempo.setCycleCount(Timeline.INDEFINITE);  // reproducción infinita
        contadorTiempo.play();                            // iniciar el contador en este momento

        // música de fondo para que se reproduzca cuando se inicia el juego
        mediaPlayerMusicaFondo.setCycleCount(MediaPlayer.INDEFINITE);  //reproducción infinita.
        mediaPlayerMusicaFondo.play();

    }

    /**
     * Acción asociada al botón <strong>Comenzar nuevo juego</strong> que
     * permite comenzar una nueva partida.
     *
     * Incluye la notación @FXML porque será accesible desde la interfaz de
     * usuario
     *
     * @param event Evento que ha provocado la llamada a este método
     */
    @FXML
    private void reiniciarJuego(ActionEvent event) {

        // detener el contador de tiempo 
        contadorTiempo.pause();
        // detener la reproducción de la música de fondo
        mediaPlayerMusicaFondo.stop();
        /* hacer visibles las 16 cartas de juego ya que es posible que no todas estén visibles 
           si se encontraron parejas en la partida anterior */
        for (Button boton : cartas) {
            boton.getStyleClass().remove("card");//Eliminar estilos iniciales
            boton.setGraphic(null);//Anulamos imagen
            boton.getStyleClass().add("fondo-transparente");
            boton.getStyleClass().add("card");
        }

        // llamar al método initialize para terminar de configurar la nueva partida
        initialize(null, null);
    }

    /**
     * Este método deberá llamarse cuando el jugador haga clic en cualquiera de
     * las cartas del tablero.
     *
     * Incluye la notación @FXML porque será accesible desde la interfaz de
     * usuario
     *
     * @param event Evento que ha provocado la llamada a este método (carta que
     * se ha pulsado)
     */
    @FXML
    private void mostrarContenidoCasilla(ActionEvent event) {
        if (permitePulsar) {
            String cartaId = ((Button) event.getSource()).getId(); // obtener el ID de la carta pulsada
            /**
             * Obtenemos la posición de la carta, como el id contiene "carta",
             * hacemos un substring para quedarnos solo con el número. Como las
             * cartas se han enumerado del 1 - 16, restamos una posición para
             * que no accedamos a una posición inexistente en la lista.
             */
            String posCarta = juego.getCartaPosicion(Integer.valueOf(cartaId.substring(5, cartaId.length())) - 1);

            // gestionar correctamente la pulsación de las cartas (si es la primera o la segunda)
            /**
             * Solo podremos tener el segundo botón pulsado, si el primero ya lo
             * está y hemos hecho clic de nuevo. Por tanto, el segundo botón
             * pasará a true siempre que el primero lo esté y se haya hecho clic
             * de nuevo.
             */
            segundoBotonPulsado = primerBotonPulsado ? true : false;
            if (segundoBotonPulsado) {
                idBoton2 = Integer.valueOf(cartaId.substring(5, cartaId.length())) - 1;
                permitePulsar = false;
            }
            primerBotonPulsado = true;
            /**
             * Si el segundo botón está a false y el primero a true, asignamos
             * el id al primer botón.
             */
            if (!segundoBotonPulsado && primerBotonPulsado) {
                idBoton1 = Integer.valueOf(cartaId.substring(5, cartaId.length())) - 1;
            }
            // descubrir la imagen asociada a cada una de las cartas (y ajustar su tamaño al tamaño del botón)

            for (Button boton : cartas) {
                if (boton.getId().equals(cartaId)) {
                    try {
                        Image image = new Image(getClass().getResourceAsStream(rutaImagenCarta + posCarta + ".png"));
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(150);
                        imageView.setFitHeight(135);
                        boton.getStyleClass().remove("card");//Eliminamos los estilos anteriores
                        boton.setGraphic(imageView);
                        boton.setAlignment(Pos.CENTER);
                        boton.setPadding(new Insets(0));
                        boton.setDisable(true);//Evitmaos que pueda pulsarse de nuevo, si está mostrada la imagen
                        boton.getStyleClass().add("aplicarOpacidad");//Aplicamos opacidad para evitar que se apliquen los estilos por defecto de un botón deshabilitado

                    } catch (NullPointerException ex) {
                        System.out.println("No existe la imagen");
                    }
                }
            }
            // identificar si se ha encontrado una pareja o no
            //Comprobaremos jugada siempre que se hay pulsado el segundo botón.

            if (segundoBotonPulsado) {
                esPareja = juego.compruebaJugada(idBoton1, idBoton2);
                // reproducir el efecto de sonido correspondiente
                if (esPareja) {
                    // sonido es pareja
                    mediaPlayerEsPareja = new MediaPlayer(mediaEsPareja);
                    mediaPlayerEsPareja.play();
                } else {
                    // sonido NO es pareja
                    mediaPlayerNoEsPareja = new MediaPlayer(mediaNoEsPareja);
                    mediaPlayerNoEsPareja.play();
                }

                // finalizar intento (usar el timeline para que haga la llamada transcurrido el tiempo definido)
                finIntento.play();
                //Dejamos a false los dos botones.
                primerBotonPulsado = false;
                segundoBotonPulsado = false;

            }
        }
    }

    /**
     * Este método permite finalizar un intento realizado. Se pueden dar dos
     * situaciones:
     * <ul>
     * <li>Si se ha descubierto una pareja: en este caso se ocultarán las cartas
     * desapareciendo del tablero. Además, se debe comprobar si la pareja
     * descubierta es la última pareja del tablero y en ese caso terminar la
     * partida.</li>
     * <li>Si NO se ha descubierto una pareja: las imágenes de las cartas deben
     * volver a ocultarse (colocando su imagen a null).</li>
     * </ul>
     * Este método será interno y NO se podrá acceder desde la interfaz, por lo
     * que NO incorpora notación @FXML.
     */
    private void finalizarIntento() {
        // hacer desaparecer del tablero las cartas seleccionadas si forman una pareja
        if (esPareja) {
            for (Button boton : cartas) {
                String idBoton = boton.getId();
                idBoton = idBoton.substring(5, idBoton.length());
                if (idBoton1 + 1 == Integer.valueOf(idBoton) || idBoton2 + 1 == Integer.valueOf(idBoton)) {
                    boton.getStyleClass().remove("card");//Eliminar estilos iniciales
                    boton.setGraphic(null);//Anulamos imagen
                }

            }
            //Si es pareja, comprobamos también si es final de partida.
            esFinalPartida = juego.compruebaFin();
            // si es final de partida mostrar el mensaje de victoria y detener el temporizador y la música
            if (esFinalPartida) {
                card.getStyleClass().add("gridPaneFinalPartida");
                // sonido Victoria
                mediaPlayerVictoria = new MediaPlayer(mediaVictoria);
                mediaPlayerVictoria.play();
                //Detener temporizador y música.
                mediaPlayerMusicaFondo.pause();
                contadorTiempo.pause();
            }
            contadorIntentos++;
        } else {// ocultar las imágenes de las cartas seleccionadas si NO forman una pareja
            for (Button boton : cartas) {
                String idBoton = boton.getId();
                idBoton = idBoton.substring(5, idBoton.length());
                if (idBoton1 + 1 == Integer.valueOf(idBoton) || idBoton2 + 1 == Integer.valueOf(idBoton)) {
                    boton.setGraphic(null);
                    boton.getStyleClass().add("card");
                    boton.setDisable(false);//Una vez oculta la carta, sí permitimos que pueda pulsarse sobre ella.
                }
            }
            contadorIntentos++;
        }
        numIntentos.setText(String.valueOf(contadorIntentos));
        permitePulsar = true;
    }

    /**
     * Este método permite salir de la aplicación. Debe mostrar una alerta de
     * confirmación que permita confirmar o rechazar la acción Al confirmar la
     * acción la aplicación se cerrará (opcionalmente, se puede incluir algún
     * efecto de despedida) Incluye la notación @FXML porque será accesible
     * desde la interfaz de usuario
     */
    @FXML
    private void salir() {

        // Alerta de confirmación que permita elegir si se desea salir o no del juego
        Stage secunStage = new Stage();

        //Hacer la ventana modal para evitar que pueda volver a la principal, sin cerrar esta.
        secunStage.initModality(Modality.APPLICATION_MODAL);

        Text texto = new Text("¿Estás seguro que deseas terminar el juego?");
        ImageView imageView = null;
        try {
            FileInputStream input = new FileInputStream(rutaIconoSalir);
            Image image = new Image(input);
            imageView = new ImageView(image);
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
        } catch (FileNotFoundException ex) {
            System.out.println("No existe la imagen");
        }
        /**
         * Crear botones confirmación
         */
        Button botonAceptar = new Button("Aceptar");
        botonAceptar.setOnAction(eve -> {
            mediaPlayerMusicaFondo.stop();
            mediaPlayerBye = new MediaPlayer(mediaBye);
            mediaPlayerBye.play();
            despedida.play();

        });
        Button botonCancelar = new Button("Cancelar");
        botonCancelar.setOnAction(eve -> {
            secunStage.close();

        });
        //Crear un layout para depositar los controles de botones sobre él
        AnchorPane panel = new AnchorPane();

        panel.setTopAnchor(texto, 30.0);
        panel.setLeftAnchor(texto, 90.0);

        panel.setTopAnchor(imageView, 10.0);
        panel.setRightAnchor(imageView, 90.0);

        panel.setTopAnchor(botonAceptar, 80.0);
        panel.setLeftAnchor(botonAceptar, 10.0);
        panel.setRightAnchor(botonAceptar, 290.0);

        panel.setTopAnchor(botonCancelar, 80.0);
        panel.setRightAnchor(botonCancelar, 10.0);
        panel.setLeftAnchor(botonCancelar, 290.0);

        panel.getChildren().addAll(texto, imageView, botonAceptar, botonCancelar);
        //Crear la escena pasándole el panel
        Scene scene = new Scene(panel, 500, 190);

        secunStage.setTitle("Salir de Memory Game!");
        secunStage.setScene(scene);
        secunStage.show();

        // SOLO si se confirma la acción se cerrará la ventana y el juego finalizará. 
        //Platform.exit();
    }
}
