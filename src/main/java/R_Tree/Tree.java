package R_Tree;

import Data.Post;

public class Tree {
    final static int BTREEDIMENSION = 2;
    Region root = new Region();
    Region aux = null;
    Post pintsAux[] = new Post[3];
    int postionAux = 0;

    public Tree() {
    }


    //Todo Insercion
    public Region insertion(Object o, Region actual) {

        if (actual.childRegionPos == 0) {
            actual.add(new Region((Post) o));
        } else {
            //Búsqueda dle mejor nodo región en el que colocar el punto
            //Todo implementar la búsqueda del mejor nodo
            bestNodeSearch(root, o);

            //comprovación de si está o no llena la mejor región
            if (!aux.isfull) {
                //si no lo está, añadimos el objeto
                //Todo implementar add
                aux.add((Post) o);
            } else {
                //En caso que no lo esté, hacemos split
                //Todo Implementar regionSplit
                Region sutituirParametros = regionSplit(null, this, (Post) o, new Region(), false);
                aux.childRegionPos = sutituirParametros.childRegionPos;
                aux.superRegion = sutituirParametros.superRegion;
                aux.pointsLeaf = sutituirParametros.pointsLeaf;
                aux.subRegions = sutituirParametros.subRegions;
                aux.childRegionPos = sutituirParametros.childPos;
                aux.isfull = sutituirParametros.isfull;
                aux.isRegionFull = sutituirParametros.isRegionFull;
                aux.childPos = sutituirParametros.childPos;
                aux.max = sutituirParametros.max;
                aux.min = sutituirParametros.min;
                aux.fatherNode = sutituirParametros.fatherNode;

                for (int i = 0; i < postionAux; i++) {
                    aux = insertion(pintsAux[i], aux.superRegion);
                }
            }
        }
        return actual;
    }

    private Region bestNodeSearch(Region actual, Object o) {
        Region regions[] = actual.subRegions;
        Region best = actual;
        Double diff = Double.MAX_VALUE;
        Post newNode = (Post) o;
        if (!actual.isLeaf) {
            for (Region region : regions) {
                if (region != null) {
                    if (region.min.x <= newNode.location[0] && region.min.y <= newNode.location[1]
                            && region.max.x >= newNode.location[0] && region.max.y >= newNode.location[1]) {
                        best = region;
                        diff = 0D;
                    } else {
                        if (diff != 0D) {
                            Point auxmax = new Point(region.max.x, region.max.y);
                            Point auxmin = new Point(region.min.x, region.min.y);
                            if (newNode.location[0] < auxmin.x) {
                                auxmin.x = newNode.location[0];
                            }
                            if (newNode.location[1] < auxmin.y) {
                                auxmin.y = newNode.location[1];
                            }
                            if (newNode.location[0] > auxmax.x) {
                                auxmax.x = newNode.location[0];
                            }
                            if (newNode.location[1] > auxmax.y) {
                                auxmax.y = newNode.location[1];
                            }
                            Double newdiff = (auxmax.x * auxmax.y) - (auxmin.x * auxmin.y);
                            if (newdiff < diff) {
                                diff = newdiff;
                                best = region;
                            }
                        }
                    }
                }
            }
            if (best.subRegions != null) {
                aux = bestNodeSearch(best, o);
            }
        }
        aux = best;
        return best;
    }

    private Region[] bestNodeSearch(Object o,Region regions[]) {
        Double diff = Double.MAX_VALUE;
        Region best = regions[0];
        Post newNode = (Post) o;
            for (Region region : regions) {
                if (region != null) {
                    if (region.min.x <= newNode.location[0] && region.min.y <= newNode.location[1]
                            && region.max.x >= newNode.location[0] && region.max.y >= newNode.location[1]) {
                        best = region;
                        diff = 0D;
                    } else {
                        if (diff != 0D) {
                            Point auxmax = new Point(region.max.x, region.max.y);
                            Point auxmin = new Point(region.min.x, region.min.y);
                            if (newNode.location[0] < auxmin.x) {
                                auxmin.x = newNode.location[0];
                            }
                            if (newNode.location[1] < auxmin.y) {
                                auxmin.y = newNode.location[1];
                            }
                            if (newNode.location[0] > auxmax.x) {
                                auxmax.x = newNode.location[0];
                            }
                            if (newNode.location[1] > auxmax.y) {
                                auxmax.y = newNode.location[1];
                            }
                            Double newdiff = (auxmax.x * auxmax.y) - (auxmin.x * auxmin.y);
                            if (newdiff < diff) {
                                diff = newdiff;
                                best = region;
                            }
                        }
                    }
                }
            }
        best.add((Post)o);
        return regions;
    }

    private Region regionSplit(Region split, Tree t, Post overflowP, Region overflowR, boolean regionsplit) {
        Region region1 = new Region();
        Region region2 = new Region();


        if (regionsplit) {
            //Región más pequeña
            Region min = new Region();
            min.min = new Point(Double.MAX_VALUE, Double.MAX_VALUE);
            //Región más grande
            Region max = new Region();
            max.max = new Point(Double.MIN_VALUE, Double.MIN_VALUE);

            //Se busca la región más cercana y lejana desde el punto de vista del 0,0
            //Para encontrar las regiones más alejadas entre ellas
            for (int i = 0; i < split.subRegions.length; i++) {
                //Búsqueda de la región más cercana

                if (split.subRegions[i].min.x < min.min.x) {
                    if (split.subRegions[i].min.y < min.min.y) {
                        min = split.subRegions[i];
                    }
                }

                //Búsqueda de la región más lejana
                if (split.subRegions[i].max.x > max.max.x) {
                    if (split.subRegions[i].max.y > max.max.y) {
                        max = split.subRegions[i];
                    }
                }
            }
            //TODO comprovar también max y min para OverflowR
            //Búsqueda de la región más cercana
            if (overflowR.min.x < min.min.x) {
                if (overflowR.min.y < min.min.y) {
                    min = overflowR;
                }
            }

            //Búsqueda de la región más lejana
            if (overflowR.max.x > max.max.x) {
                if (overflowR.max.y > max.max.y) {
                    max = overflowR;
                }
            }
            //Fin de este TODO

            region1.add(min);
            region2.add(max);
            //TODO añadir region1 y region2 a una región como subregiones.

            //Fin del TODO

            //TODO cambiar el condicional y subir el for
            if (!split.superRegion.isfull) {
                //bucle para encontrar dónde poner cada región
                for (int i = 0; i < split.superRegion.subRegions.length; i++) {
                    if ((split.superRegion.subRegions[i] != max) || (split.superRegion.subRegions[i] != min)) {
                        if (region1.newArea(split.superRegion.subRegions[i]) < region2.newArea(split.superRegion.subRegions[i])) {
                            region1.add(split.superRegion.subRegions[i]);
                        } else {
                            region2.add(split.superRegion.subRegions[i]);
                        }
                    }
                }

                //Insertamos la región de overflow
                //TODO comprovar si la región de overflow es max o min
                if (region1.newArea(overflowR) < region2.newArea(overflowR)) {
                    region1.add(overflowR);
                } else {
                    region2.add(overflowR);
                }

            } else {
                regionSplit(split.fatherNode.superRegion, this, overflowP, overflowR, true);
            }
        } else {
            split = aux;
            Double distanciaMin = Double.MAX_VALUE;
            Double distanciaMax = Double.MIN_VALUE;
            Double distancia = null;
            //Búsqueda de los dos posts más distanciados, es decir, aquellos que forman la MBR
            //max y min dentro de la región.
            Post min = null;
            Post max = null;
            for (int i = 0; i < split.childPos; i++) {
                //Comprovación para el punto mínimo
                //Calculo de distancias
                distancia = calcularDistanciaDesde(split.pointsLeaf[i].location);
                if (distancia <= distanciaMin) {
                    distanciaMin = distancia;
                    min = split.pointsLeaf[i];
                }

                //Comprovación para el punto máximo
                if (distancia >= distanciaMax) {
                    distanciaMax = distancia;
                    max = split.pointsLeaf[i];
                }
            }

            distancia = calcularDistanciaDesde(overflowP.location);
            if (distancia <= distanciaMin) {
                min = overflowP;
            }

            //Comprovación para el punto máximo
            if (distancia >= distanciaMax) {
                max = overflowP;
            }

            //Comprovación con el punto que he de añadir (OverflowP)

            region1 = new Region(min);
            region2 = new Region(max);
            region1.superRegion = split.superRegion;
            region2.superRegion = split.superRegion;

            Region aux2 = (Region) split.clone();
            //En caso que haya sitio para las nuevas regiones, se ponen
            if (!(split.superRegion.isRegionFull)) {
                //Búsqueda e inserción de ambas regiones
                split.superRegion.add(region2);
                split = region1;
                postionAux = 0;
                //Redistribución de los puntos en las nuevas regiones
                for (int i = 0; i < aux2.pointsLeaf.length; i++) {
                    if (!((double) aux2.pointsLeaf[i].location[0] == (double) region1.pointsLeaf[0].location[0] && (double) aux2.pointsLeaf[i].location[1] == (double) region1.pointsLeaf[0].location[1])) {
                        if (!((double) aux2.pointsLeaf[i].location[0] == (double) region2.pointsLeaf[0].location[0] && (double) aux2.pointsLeaf[i].location[1] == (double) region2.pointsLeaf[0].location[1])) {
                            pintsAux[postionAux] = aux2.pointsLeaf[i];
                            postionAux++;
                        }
                    }
                }

                //TODO añadir el punto OverflowP
                if (!((double) overflowP.location[0] == (double) region1.pointsLeaf[0].location[0] && (double) overflowP.location[1] == (double) region1.pointsLeaf[0].location[1])) {
                    if (!((double) overflowP.location[0] == (double) region2.pointsLeaf[0].location[0] && (double) overflowP.location[1] == (double) region2.pointsLeaf[0].location[1])) {
                        pintsAux[postionAux] = overflowP;
                        postionAux++;
                    }
                }
                //Fin del TODO

                return split;
            } else {
                //TODO asignar overflowR

                //Fin del TODO

                postionAux = 0;
                //Redistribución de los puntos en las nuevas regiones
                for (int i = 0; i < aux2.pointsLeaf.length; i++) {
                    if (!((double) aux2.pointsLeaf[i].location[0] == (double) region1.pointsLeaf[0].location[0] && (double) aux2.pointsLeaf[i].location[1] == (double) region1.pointsLeaf[0].location[1])) {
                        if (!((double) aux2.pointsLeaf[i].location[0] == (double) region2.pointsLeaf[0].location[0] && (double) aux2.pointsLeaf[i].location[1] == (double) region2.pointsLeaf[0].location[1])) {
                            pintsAux[postionAux] = aux2.pointsLeaf[i];
                            postionAux++;
                        }
                    }
                }

                //TODO añadir el punto OverflowP
                if (!((double) overflowP.location[0] == (double) region1.pointsLeaf[0].location[0] && (double) overflowP.location[1] == (double) region1.pointsLeaf[0].location[1])) {
                    if (!((double) overflowP.location[0] == (double) region2.pointsLeaf[0].location[0] && (double) overflowP.location[1] == (double) region2.pointsLeaf[0].location[1])) {
                        pintsAux[postionAux] = overflowP;
                        postionAux++;
                    }
                }

                Region[] auxiliar =  new Region[]{region1,region2};
                auxiliar = bestNodeSearch(pintsAux[postionAux-1],auxiliar);
                aux.childRegionPos = auxiliar[0].childRegionPos;
                aux.superRegion = auxiliar[0].superRegion;
                aux.pointsLeaf = auxiliar[0].pointsLeaf;
                aux.subRegions = auxiliar[0].subRegions;
                aux.childRegionPos = auxiliar[0].childPos;
                aux.isfull = auxiliar[0].isfull;
                aux.isRegionFull = auxiliar[0].isRegionFull;
                aux.childPos = auxiliar[0].childPos;
                aux.max = auxiliar[0].max;
                aux.min = auxiliar[0].min;
                aux.fatherNode = auxiliar[0].fatherNode;
                regionSplit(split.superRegion, this, overflowP, auxiliar[1], true);

                //Fin del TODO

            }

        }
        return split;
    }

    public double calcularDistanciaDesde(Double[] parametros) {
        double cateto1 = parametros[0];
        double cateto2 = parametros[1];
        double hipotenusa = Math.sqrt(cateto1 * cateto1 + cateto2 * cateto2);
        return hipotenusa;

    }
}
