

//import net.lingala.zip4j.ZipFile;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {
    public static String url = "https://www.dgii.gov.do/app/WebApps/Consultas/RNC/DGII_RNC.zip";
    public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static LocalDateTime now = LocalDateTime.now();
    public static Map<String, RazonSocial> razonSocialList = new HashMap<String, RazonSocial>();
    public static String ubi = "\\";
//    public static List<RazonSocial> razonSocialList = new ArrayList<RazonSocial>();


    public static void main(String[] args) {

        System.out.println(dtf.format(now));

        try {
//            downloadUsingNIO(url, "F:\\DGII_RNC.zip");
            try {
                String ruta = ubi+"dgiifilefecha.txt";
                String rutaDescarga = ubi+"DGII_RNC.zip";

                File archivo = new File(ruta);

                BufferedWriter bw = null;
                if (archivo.exists()) {
                    System.out.println("Entro");
                    File archivoFecha = null;
                    FileReader fr = null;
                    BufferedReader br = null;
                    String fech = "";


                    try {
                        // Apertura del fichero y creacion de BufferedReader para poder
                        // hacer una lectura comoda (disponer del metodo readLine()).

                        fr = new FileReader (archivo);
                        br = new BufferedReader(fr);

                        // Lectura del fichero
                        String linea;
                        while((linea=br.readLine())!=null) {

                            System.out.println(linea);
                            fech = linea;

                        }
                        System.out.println("La fecha file es:"+fech);
                        if (fech.equalsIgnoreCase(dtf.format(now))){
                            saveFileDGIItoList(rutaDescarga);
                            RazonSocial razonSocial =  ( RazonSocial) razonSocialList.get("130012768");

                            if (razonSocial!=null){
                                System.out.println("\nAparecio");
                            }

                        }else{
                            bw = new BufferedWriter(new FileWriter(archivo));
                            bw.write(dtf.format(now));
                            downloadUsingNIO(url, rutaDescarga);
                            saveFileDGIItoList(rutaDescarga);
                            bw.close();
                            RazonSocial razonSocial =  ( RazonSocial) razonSocialList.get("130012768");

                            if (razonSocial!=null){
                                System.out.println("\nAparecio");
                            }
                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }finally{
                        // En el finally cerramos el fichero, para asegurarnos
                        // que se cierra tanto si todo va bien como si salta
                        // una excepcion.
                        try{
                            if( null != fr ){
                                fr.close();
                            }
                        }catch (Exception e2){
                            e2.printStackTrace();
                        }
                    }



//                    bw = new BufferedWriter(new FileWriter(archivo));
//                    bw.write("El fichero de texto ya estaba creado.");
                } else {
                    System.out.println("No encontro fecha");
                    downloadUsingNIO(url, rutaDescarga);
                    bw = new BufferedWriter(new FileWriter(archivo));
                    bw.write(dtf.format(now));
                    saveFileDGIItoList(rutaDescarga);
                    bw.close();
                }

            } catch (IOException ioException) {
                ioException.printStackTrace();
            }



//            new ZipFile(rutaDescarga).extractFile("DGII_RNC.TXT".toString(), ubi+"");
//            new ZipFile(rutaDescarga).extractFile("folderNameInZip/", "/destination_directory");

//            downloadUsingStream(url, ubi+"DGII_RNC_Stream.zip");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveFileDGIItoList(String rutaDescarga){
        String fileZip = rutaDescarga;
        String destDir ="";
        unzip(fileZip, destDir);
        try {
            File file = new File(ubi+"TMP\\DGII_RNC.TXT");

            BufferedReader br = new BufferedReader(new FileReader(file));

            String st;
            int count = 0;
            int salir = 0;
            int posi = 1;
            while ((st = br.readLine()) != null) {
                RazonSocial razonSocial = new RazonSocial();
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < st.length(); i++) {
//                    System.out.println("Char " + i + " is " + st.charAt(i));

                    if (st.charAt(i) == '|' || i == (st.length() - 1)) {

                        if (i == (st.length() - 1)) {
                            stringBuilder.append(st.charAt(i));
                        }
                        count += 1;
                        salir=0;
                        switch (count) {

                            case 1:
                                razonSocial.setRnc(stringBuilder.toString());
                                stringBuilder = new StringBuilder();
                                break;
                            case 2:
                                razonSocial.setNombreRazonSocial(stringBuilder.toString());
                                stringBuilder = new StringBuilder();
                                break;
                            case 3:
                                razonSocial.setNombreComercial(stringBuilder.toString());
                                stringBuilder = new StringBuilder();
                                break;
                            case 4:
                                razonSocial.setActividadEconomica(stringBuilder.toString());
                                stringBuilder = new StringBuilder();
                                break;
                            case 5:

                                stringBuilder = new StringBuilder();
                                break;
                            case 6:

                                stringBuilder = new StringBuilder();
                                break;
                            case 7:

                                stringBuilder = new StringBuilder();
                                break;
                            case 8:

                                stringBuilder = new StringBuilder();
                                break;
                            case 9:
                                razonSocial.setFechaInicio(stringBuilder.toString());
                                stringBuilder = new StringBuilder();
                                break;
                            case 10:
                                razonSocial.setEstado(stringBuilder.toString());
                                stringBuilder = new StringBuilder();
                                break;

                            case 11:
                                try {
                                    razonSocial.setRegimenPago(stringBuilder.toString());
                                    stringBuilder = new StringBuilder();
                                }catch (Exception e){
                                    stringBuilder = new StringBuilder();
                                }

                                break;
                            default:
                                break;

                        }

                    } else {
//                        System.out.println("Agrego");
                        stringBuilder.append(st.charAt(i));
                        if(st.charAt(i)== ' '){
                            salir +=10;
                        }

                    }
                    if (salir == 5){
                        System.out.println("\nEspacio en Blanco ult cant"+count+"posi:"+posi+"String"+stringBuilder.toString());
                        break;
                    }
                }
                razonSocialList.put(razonSocial.getRnc(),razonSocial);
                count = 0;
                stringBuilder = new StringBuilder();

//                System.out.println("Linea" + posi + "-" + dtf.format(now) + ": " + razonSocial.getRnc() + "|" + razonSocial.getNombreRazonSocial() + "|" + razonSocial.getFechaInicio() + "|" + razonSocial.getEstado() + "|" + razonSocial.getRegimenPago());
                posi += 1;
            }

        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private static void unzip(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if (!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                System.out.println("Unzipping to " + newFile.getAbsolutePath());
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static void downloadUsingStream(String urlStr, String file) throws IOException {
        URL url = new URL(urlStr);
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        FileOutputStream fis = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int count = 0;
        while ((count = bis.read(buffer, 0, 1024)) != -1) {
            fis.write(buffer, 0, count);
        }
        fis.close();
        bis.close();
    }

    private static void downloadUsingNIO(String urlStr, String file) throws IOException {
        URL url = new URL(urlStr);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(file);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
    }
}
