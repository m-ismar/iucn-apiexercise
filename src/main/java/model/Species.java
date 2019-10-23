package model;

/**
 * A simple model to map the API response
 */
public class Species {

    private Long taxonid;
    private String kingdom_name;
    private String phylum_name;
    private String class_name;
    private String order_name;
    private String family_name;
    private String genus_name;
    private String scientific_name;
    private String infra_rank;
    private String infra_name;
    private String population;
    private String category;

    private String conservation_measures;

    @Override
    public String toString() {
        return "ID: " + taxonid
                + "; Kingdom: " + kingdom_name
                + "; Phylum: " + phylum_name
                + "; Class: " + class_name
                + "; Order: " + order_name
                + "; Family: " + family_name
                + "; Genus: " + genus_name
                + "; Scientific name: " + scientific_name
                + "; Infra rank: " + (infra_rank == null ? "N/A" : infra_rank)
                + "; Infra name: " + (infra_name == null ? "N/A" : infra_name)
                + "; Population: " + (population == null ? "N/A" : population)
                + "; Category: " + category
                + (conservation_measures == null ? ";" : "; Conservation measures: " + conservation_measures + ";");
    }

    public Long getTaxonid() {
        return taxonid;
    }

    public void setTaxonid(Long taxonid) {
        this.taxonid = taxonid;
    }

    public String getKingdom_name() {
        return kingdom_name;
    }

    public void setKingdom_name(String kingdom_name) {
        this.kingdom_name = kingdom_name;
    }

    public String getPhylum_name() {
        return phylum_name;
    }

    public void setPhylum_name(String phylum_name) {
        this.phylum_name = phylum_name;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getOrder_name() {
        return order_name;
    }

    public void setOrder_name(String order_name) {
        this.order_name = order_name;
    }

    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    public String getGenus_name() {
        return genus_name;
    }

    public void setGenus_name(String genus_name) {
        this.genus_name = genus_name;
    }

    public String getScientific_name() {
        return scientific_name;
    }

    public void setScientific_name(String scientific_name) {
        this.scientific_name = scientific_name;
    }

    public String getInfra_rank() {
        return infra_rank;
    }

    public void setInfra_rank(String infra_rank) {
        this.infra_rank = infra_rank;
    }

    public String getInfra_name() {
        return infra_name;
    }

    public void setInfra_name(String infra_name) {
        this.infra_name = infra_name;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getConservation_measures() {
        return conservation_measures;
    }

    public void setConservation_measures(String conservation_measures) {
        this.conservation_measures = conservation_measures;
    }


}
