package buchtajosef.meteorsites;

import java.util.Comparator;


class MeteorMassComparator implements Comparator<MeteorData> {
    @Override
    public int compare(MeteorData o1, MeteorData o2) {
        Integer m1 = o1.getMass();
        Integer m2 = o2.getMass();
        return m1.compareTo(m2);
    }
}
