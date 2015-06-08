package net.yosef.service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import jdk.nashorn.internal.ir.debug.JSONWriter;
import jdk.nashorn.internal.parser.JSONParser;
import net.yosef.domain.User;
import net.yosef.repository.UserRepository;
import net.yosef.security.SecurityUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by user on 27/05/15.
 */
@Controller
public class ImatgeController {
    @Inject
    private UserRepository userepo;

    public static final String CARPETA_IMATGES = "src/main/webapp/imgs";
    public static final String DESTI_PUBLIC = "imgs";
    private User user = null;

    /**
     * @return la imatge que te l'usuari logejat!
     */
    @RequestMapping(value = "/imatge", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity<String> provideImage()  {
        return new ResponseEntity<>("Aqui es pot pujar imatges",HttpStatus.OK);
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
                            if (user.getImg() != null){
                                comprovarImatgeUser(new File(user.getImg()));
                            }
                            user.setImg(DESTI_PUBLIC + File.separator + desti.getName());
                            userepo.save(user);
                            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(desti));
                            stream.write(bytes);
                            stream.close();
                            return "{\"msg\":\"S'ha pujat correctament " + file.getOriginalFilename() + "!\"}";
                        } else {
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

    private void comprovarImatgeUser(File img) {
        //comprovem si ja tenia una imatge.

        //comprovar si existei amb un altre ext i eliminarlo
        if (img.exists()) {
            // l'esborrem
            img.delete();

        }
    }
}
