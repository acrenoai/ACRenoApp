package rs.acreno.system.util;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import rs.acreno.racuni.faktura.FakturaController;
import rs.acreno.system.constants.Constants;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GeneralUiUtility {

    /**
     * Cisti podatke iz svih TextFiel Polja u nekom Panelu odjednom.
     * Ovde se koristi:
     *
     * @param pane Pane u kom se nalaze TextField polja.
     */
    public static void clearTextFieldsInPane(@NotNull Pane pane) {
        for (Node node : pane.getChildren()) {
            if (node instanceof TextField) {
                ((TextField) node).setText("");
                node.setDisable(false);
            }
            if (node instanceof TextArea) {
                ((TextArea) node).setText("");
            }
        }
    }

    /**
     * AlertBox kada se uradi akcija na nekom dugmetu
     *
     * @param alertType  Tip alerta Alert.AlertType.INFORMATION ili vec
     * @param headerText Naslov hedera
     * @param title      Naslov poruke
     * @param poruka     Poruka u alert
     * @return Alert
     */
    public static @NotNull Alert alertDialogBox(Alert.AlertType alertType, String headerText, String title, String poruka) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(poruka);
        ((Stage)alert.getDialogPane().getScene().getWindow()).getIcons().add(new Image(Constants.APP_ICON));
        alert.show();
        return alert;
    }

    /**
     * TODO: Napisati Java Doc
     *
     * @param ancorPanePrint
     * @param btnPrint
     * @param closePrint
     */
    public static void printStaff(AnchorPane ancorPanePrint, Button btnPrint, Button closePrint) {
        PrinterJob printJobRadniNalog = PrinterJob.createPrinterJob();

        if (printJobRadniNalog != null && printJobRadniNalog.showPrintDialog(ancorPanePrint.getScene().getWindow())) {
            btnPrint.setVisible(false);
            closePrint.setVisible(false);
            Printer printer = printJobRadniNalog.getPrinter();
            printJobRadniNalog.getJobSettings().setPrintQuality(PrintQuality.HIGH);
            PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);

            double scaleX = pageLayout.getPrintableWidth() / ancorPanePrint.getBoundsInParent().getWidth();
            ancorPanePrint.getTransforms().add(new Scale(scaleX, scaleX));

            boolean success = printJobRadniNalog.printPage(pageLayout, ancorPanePrint);
            if (success) {
                printJobRadniNalog.endJob();
                GeneralUiUtility.alertDialogBox(Alert.AlertType.INFORMATION
                        , "USPEŠAN PRINT"
                        , "PRINT"
                        , "Uspešno Printanje" + printJobRadniNalog.getJobStatus());
            } else {
                GeneralUiUtility.alertDialogBox(Alert.AlertType.ERROR
                        , "GREŠKA PRINT"
                        , "PRINT"
                        , "GREŠKA U PRINTU" + printJobRadniNalog.getJobStatus());
            }
            btnPrint.setVisible(true);
            closePrint.setVisible(true);
        }
    }

    /**
     * Promena datum a u ono sto nama treba za Srbiju. FORMAT "dd.MM.yyyy"
     * Koristimo svuda gde treba da se postavi datum...npr. iz DatePckera u bazu pa jedna od koriscenja je
     * {@link rs.acreno.racuni.faktura.FakturaController #newOrEditRacun} i u
     * {@link FakturaController #btnSacuvajRacunAction()}
     *
     * @param date uzima LocalDate i vraca Formatiran String
     */
    public static @NotNull String formatDateForUs(@NotNull LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return date.format(formatter);
    }

    /**
     * Promena datum a u ono sto nama treba za Srbiju. FORMAT "dd.MM.yyyy"
     * Koristimo svuda gde treba da se postavi datum...npr. iz DatePckera u bazu pa jedna od koriscenja je
     * {@link rs.acreno.racuni.faktura.FakturaController #newOrEditRacun} i u
     * {@link FakturaController #btnSacuvajRacunAction()}
     *
     * @param string Uzima String i vraca formatiran LocalDate
     */
    public static @Nullable LocalDate fromStringDate(String string) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.parse(string, formatter);
    }

    /**
     * //TODO: Napisati DOC
     *
     * @param sum double koji se formatra
     * @return String pa se posle radi parse to Double
     */
    public static String formatDecimalPlaces(double sum) {
        return new DecimalFormat("###,###.00").format(sum);
    }

    /**
     * Postavljenje stat koji se prikazuje u Labelu
     * @param label gde prikazijemo sat
     * @param formatter format sata
     */
    public static void initSat(Label label, DateTimeFormatter formatter) {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            label.setText(LocalDateTime.now().format(formatter));
            label.setAlignment(Pos.CENTER);
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
}

