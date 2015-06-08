package net.yosef.service.lliga;

import net.yosef.domain.Equip;
import net.yosef.domain.Grup;
import net.yosef.domain.Jornada;
import net.yosef.domain.Partit;
import net.yosef.repository.EquipRepository;
import net.yosef.repository.GrupRepository;


import javax.inject.Inject;
import java.util.*;

import net.yosef.repository.JornadaRepository;
import net.yosef.repository.PartitRepository;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Controller;

/**
 * Created by user on 23/05/15.
 */
public class GeneradorJornades {

    private ArrayList<Equip> equips;
    private List<Jornada> jornades;
    private Grup grup;
//    private Random rnd;
    private LocalDate d_inici;

    //    @Inject
    private PartitRepository partitRepository;

    //    @Inject
    private JornadaRepository jornadaRepository;

    public GeneradorJornades() {

    }

    public GeneradorJornades(Grup g, LocalDate d_inici) {
        equips = new ArrayList<>();
        jornades = new ArrayList<>();
        grup = g;
//        rnd = new Random();
        if (d_inici == null) {
            this.d_inici = LocalDate.now();
        } else {
            this.d_inici = d_inici;

        }

        if (g != null && !g.getEquips().isEmpty()) {

            if (g.getEquips().size() > 3 && g.getEquips().size() % 2 == 0) {
                equips.addAll(g.getEquips());
            } else {
                throw new NullPointerException("Els equips han de ser 4 o mes i parells!");
            }
        }
    }

    public boolean generarLliga() {
        List<Partit> partits = new ArrayList();
        int girar = 0;
//        LocalDate d_inici = new LocalDate(2015, 06, 01);

        for (int i = 1; i < equips.size(); i++) {
            //creem una joranada sense partits, la guardem per obtenir id
            Jornada jorn = new Jornada();
            jorn.setNumero(i);
            jorn.setGrup(grup);
            jornadaRepository.save(jorn);

            generarJornada(partits, girar, d_inici, jorn);
            rotar();
            girar = (girar + 1) % 2;

            //persistem els partits per obtenir id dels partits
            partitRepository.save(partits);

            //afegim la jorn amb els partits al array

            jorn.setPartits(partits);

            jornades.add(jorn);
            partits.clear();
            d_inici = d_inici.plusDays(7);
        }

        for (Jornada j : jornades) {
            System.out.println("Jornada : " + j.getNumero());
            System.out.println("-----------");
            j.getPartits().forEach(
                partit -> System.out.println(partit.getNom_l() + " vs " + partit.getNom_v() + " Dia : " + partit.getData())
            );
            System.out.println();
        }
        jornadaRepository.save(jornades);
        return true;
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

    private void generarJornada(List<Partit> partits, int girar, LocalDate d, Jornada jorn) {
        int numPartits = getPartitsPerJornada();
        int last = equips.size() - 1;
        for (int i = 0; i < numPartits; i++) {
            Partit tmp;
            if (girar == 0) {
                tmp = new Partit(equips.get(i), equips.get(last));
            } else {
                tmp = new Partit(equips.get(i), equips.get(last));
            }
            tmp.setData(d);
            tmp.setJornada(jorn);
            //save to get id, sino, no es pot afegir al set
//            tmp.setId(rnd.nextLong());
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


    public void setPartitRepository(PartitRepository partitRepository) {
        this.partitRepository = partitRepository;
    }

    public void setJornadaRepository(JornadaRepository jornadaRepository) {
        this.jornadaRepository = jornadaRepository;
    }
}
