package co.edu.unicauca.review.model;

/**
 * Clase FormatoEvaluacion definida en el diagrama UML (Cluster: Revision por Pares).
 * Atributos:
 * - id: Long
 * - cumpleCalidadPedagogica: boolean
 * - cumpleCalidadTecnica: boolean
 * - cumpleCalidadEstructural: boolean
 * - puntajeTotal: int
 * Metodo:
 * + esValidoParaAprobacion(): boolean
 */
public class FormatoEvaluacion {
    private Long id;
    private boolean cumpleCalidadPedagogica;
    private boolean cumpleCalidadTecnica;
    private boolean cumpleCalidadEstructural;
    private int puntajeTotal;

    public FormatoEvaluacion() {
    }

    public FormatoEvaluacion(Long id, boolean cumpleCalidadPedagogica, boolean cumpleCalidadTecnica, boolean cumpleCalidadEstructural, int puntajeTotal) {
        this.id = id;
        this.cumpleCalidadPedagogica = cumpleCalidadPedagogica;
        this.cumpleCalidadTecnica = cumpleCalidadTecnica;
        this.cumpleCalidadEstructural = cumpleCalidadEstructural;
        this.puntajeTotal = puntajeTotal;
    }

    /**
     * Metodo del diagrama UML: +esValidoParaAprobacion(): boolean
     */
    public boolean esValidoParaAprobacion() {
        return cumpleCalidadPedagogica
                && cumpleCalidadTecnica
                && cumpleCalidadEstructural
                && puntajeTotal >= 70;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isCumpleCalidadPedagogica() {
        return cumpleCalidadPedagogica;
    }

    public void setCumpleCalidadPedagogica(boolean cumpleCalidadPedagogica) {
        this.cumpleCalidadPedagogica = cumpleCalidadPedagogica;
    }

    public boolean isCumpleCalidadTecnica() {
        return cumpleCalidadTecnica;
    }

    public void setCumpleCalidadTecnica(boolean cumpleCalidadTecnica) {
        this.cumpleCalidadTecnica = cumpleCalidadTecnica;
    }

    public boolean isCumpleCalidadEstructural() {
        return cumpleCalidadEstructural;
    }

    public void setCumpleCalidadEstructural(boolean cumpleCalidadEstructural) {
        this.cumpleCalidadEstructural = cumpleCalidadEstructural;
    }

    public int getPuntajeTotal() {
        return puntajeTotal;
    }

    public void setPuntajeTotal(int puntajeTotal) {
        this.puntajeTotal = puntajeTotal;
    }
}
