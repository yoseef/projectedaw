package net.yosef.service;

import java.io.*;

import net.yosef.domain.User;
import net.yosef.repository.UserRepository;
import net.yosef.security.SecurityUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;

/**
 * Created by user on 27/05/15.
 */
@Controller
public class ImatgeController {
    @Inject
    private UserRepository userepo;
    public static final String CARPETA_IMATGES = "imatges";
    private User user = null;


    /**
     * @return la imatge que te l'usuari logejat!
     */
    @RequestMapping(value = "/imatge", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity<byte[]> provideImage() {
        InputStream in = null;
        MediaType mediatype;
        File imatge = obtenirImatgeDeUser();
        if (imatge != null) {
            try {
                in = new FileInputStream(
                    imatge
                );
                mediatype = obtenirFormatImatge(imatge.getName());
                return ResponseEntity.ok().contentType(mediatype).body(IOUtils.toByteArray(in));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * @param file
     * @return
     */
    @RequestMapping(value = "/imatge", method = RequestMethod.POST)
    public
    @ResponseBody
    String handleFileUpload(@RequestParam("file") MultipartFile file) {
//        if (userepo.findOneByLogin(SecurityUtils.getCurrentLogin()).isPresent()) {
        user = getCurrentUser();
        if (user != null) {
            if (!file.isEmpty()) {
                try {
                    crearCarpetaImatge();
                    if (filtreImatge(file.getContentType())) {
                        File desti = assignarRutaDesti(user.getLogin(), file.getContentType().split("/")[1]);
                        if (desti != null) {
                            byte[] bytes = file.getBytes();
                            //eliminar la anterior
                            comprovarImatgeUser(new File(user.getImg()));
                            user.setImg(desti.getAbsolutePath());
                            userepo.save(user);
                            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(desti));
                            stream.write(bytes);
                            stream.close();
                            return "{\"msg\":\"S'ha pujat correctament " + file.getOriginalFilename() + "!\"}";
                        }else {
                            return "{\"msg\":\"No s'ha pogut guardar la ruta de la imatge!\"}";
                        }
                    } else {
                        return "{\"msg\":\"No s'accepta aquest format de la imatge ha de ser : jpg o png!\"}";
                    }

                } catch (Exception e) {
                    return "{\"msg\":\"Ha fallat la pujada del fitxer " + file.getOriginalFilename() + " => " + e.getMessage() + "\"}";
                }
            } else {
                return "{\"msg\":\"Ha fallat la pujada del fitxer : " + file.getOriginalFilename() + " perque el fitxer esta buit.\"";
            }
        } else {
            return "{\"msg\":\"Ha fallat la pujada del fitxer, l'usuari no s'ha identificat!.\"";
        }


//        } else {
//            return "{\"msg\":\"Ha fallat la pujada del fitxer : " + file.getOriginalFilename() + " usuari no authentificat.\"";
//        }
    }

    // Funcions auxiliars
    private User getCurrentUser() {
        if (userepo.findOneByLogin(SecurityUtils.getCurrentLogin()).isPresent()) {
            return userepo.findOneByLogin(SecurityUtils.getCurrentLogin()).get();
        }
        return null;
    }

    private File obtenirImatgeDeUser() {
        File imatge = null;
        user = getCurrentUser();
        if (user != null) {
            if (user.getImg() != null) {
                imatge = new File(user.getImg());
                if (imatge.exists()) {
                    return imatge;
                }
            }
        }
        return imatge;
    }

    private MediaType obtenirFormatImatge(String imatge) {
        //default jpeg
        MediaType mediatype = MediaType.IMAGE_JPEG;
        if (imatge != null && !imatge.equals("")) {
            if (imatge.contains(".jpg") || imatge.contains(".jpeg")) {
                mediatype = MediaType.IMAGE_JPEG;
            } else if (imatge.contains(".png")) {
                mediatype = MediaType.IMAGE_PNG;
            } else if (imatge.contains(".gif")) {
                mediatype = MediaType.IMAGE_GIF;
            }
        }
        return mediatype;
    }

    private void crearCarpetaImatge() {
        File CarpetaImatges = new File(CARPETA_IMATGES);
        if (!CarpetaImatges.exists() || !CarpetaImatges.isDirectory()) {
            CarpetaImatges.mkdir();
        }
    }

    private boolean filtreImatge(String format) {
        if (format.equals("image/jpeg") || format.equals("image/png") || format.equals("image/gif")) {
            return true;
        }
        return false;
    }

    private File assignarRutaDesti(String user, String ext) {
        File f = null;
        if (!user.equals("") && !ext.equals("")) {
            f = new File(CARPETA_IMATGES + File.separator + user + "." + ext);
            return f;
        }
        return f;
    }
    private void comprovarImatgeUser(File img){
        //comprovem si ja tenia una imatge.
        if(img.exists()){
            // l'esborrem
            img.delete();
        }
    }
}
