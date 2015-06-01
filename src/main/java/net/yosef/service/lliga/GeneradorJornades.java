package net.yosef.service.lliga;

import net.yosef.domain.Equip;
import net.yosef.domain.Grup;
import net.yosef.domain.Jornada;
import net.yosef.domain.Partit;
import net.yosef.repository.EquipRepository;
import net.yosef.repository.GrupRepository;


import javax.inject.Inject;
import java.util.*;
import org.joda.time.LocalDate;

/**
 * Created by user on 23/05/15.
 */
public class GeneradorJornades {

    static ArrayList<Equip> equips;
    Random rnd = new Random();

//    public static void main(String[] args) {
//        GeneradorJornades gj = new GeneradorJornades();
//        equips = new ArrayList<>();
//        gj.addEquips();
//        gj.generarLliga();
////        gj.imprimir();
//
//    }


    public void addEquips() {
        LocalDate d = new LocalDate();
        Grup g = new Grup("Dilluns");

        Equip verd = new Equip("verd", d, g);
        Equip vermell = new Equip("vermell", d, g);
        Equip blau = new Equip("blau", d, g);
        Equip negre = new Equip("negre", d, g);
        Equip rosa = new Equip("rosa", d, g);
        Equip groc = new Equip("groc", d, g);
        Equip taronja = new Equip("taronja", d, g);
        Equip blanc = new Equip("blanc", d, g);

        equips.add(verd);
        equips.add(vermell);
        equips.add(blau);
        equips.add(blanc);
        equips.add(negre);
        equips.add(groc);
        equips.add(rosa);
        equips.add(taronja);
    }

    public List<Jornada> generarLliga() {
        List<Jornada> jornades = new ArrayList<>();
        List<Partit> partits = new ArrayList();
        int girar = 0;
        LocalDate d_inici = new LocalDate(2015,06,01);
        for (int i = 1; i < equips.size(); i++) {

            generarJornada(partits, girar,d_inici);
            rotar();
            girar = (girar + 1) % 2;
            jornades.add(new Jornada(i, partits));
            partits.clear();
            d_inici = d_inici.plusDays(7);
        }

        for (Jornada j : jornades) {
            System.out.println("Jornada : " + j.getNumero());
            System.out.println("-----------");
            j.getPartits().forEach(
                partit -> System.out.println(partit.getNom_l() + " vs " + partit.getNom_v() + " Dia : "+ partit.getData())
            );
            System.out.println();
        }
        return jornades;
    }

    private void rotar() {
        Equip actual = equips.get(1);
        for (int i = 2; i < equips.size(); i++) {
            Equip nou = equips.get(i);
            equips.set(i, actual);
            actual = nou;
        }
        equips.set(1, actual);
    }

    private void generarJornada(List<Partit> partits, int girar, LocalDate d) {
        int numPartits = getPartitsPerJornada();
        int last = equips.size() - 1;
        for (int i = 0; i < numPartits; i++) {
            Partit tmp;
            if (girar == 0) {
                tmp = new Partit(equips.get(i), equips.get(last));
            } else {
                tmp = new Partit(equips.get(i), equips.get(last));
            }
            tmp.setId(rnd.nextLong());
            tmp.setData(d);
            partits.add(tmp);

            last--;
        }
    }

    public int getPartitsPerJornada() {
        if (equips != null && equips.size() > 0) {
            return equips.size() / 2;
        }
        return -1;
    }

}
