package buchtajosef.meteorsites;

//holds data of one meteor
class MeteorData {
    private String name, nameType, recClass, fall, year, reclat, reclong;
    private int id, mass;
    private boolean selected = false;
    private double[] coordinates;

    MeteorData (int id, String name, String nameType, String recClass, String fall, String year, String reclat, String reclong, int mass, double[] coordinates) {
        this.id = id;
        this.name = name;
        this.nameType = nameType;
        this.recClass = recClass;
        this.fall = fall;
        this.year = year;
        this.reclat = reclat;
        this.reclong = reclong;
        this.mass = mass;
        this.coordinates = coordinates;
    }

    int getMass () {
        return mass;
    }

    String getMassText () {
        return String.valueOf(mass);
    }

    String getName () {
        return name;
    }

    String getYear () {
        return year;
    }

    String getId () {
        return String.valueOf(id);
    }

    int getIntID () {
        return id;
    }

    String getCoordinatesText () {
        return String.valueOf(coordinates[0]) + "  " + String.valueOf(coordinates[1]);
    }

    double[] getCoordinates () {
        return coordinates;
    }

    void setSelected (boolean state) {
        selected = state;
    }

    boolean isSelected () {
        return selected;
    }
}
