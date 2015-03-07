package com.ucl.search.sbr.services.transition_model_builder;


import com.ucl.search.sbr.services.entityExtraction.Entity;
import com.ucl.search.sbr.services.entityExtraction.Interaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ralucamelon on 05/03/2015.
 */
public class EntityTypeExtractor {

    public EntityTypeExtractor() {
    }


    public List<Entity> extractThemeEntities(Interaction interaction1, Interaction interaction2) {

        List<Entity> themeEntities = new ArrayList<>();

        Entity[] Eq1 = interaction1.getEntities();
        Entity[] Eq2 = interaction2.getEntities();

        /* get the theme entities */

        for (Entity e1 : Eq1) {
            for (Entity e2 : Eq2)
                if (e1.getMention().equals(e2.getMention())) {
                    themeEntities.add(e1);
                }
        }

        return themeEntities;
    }

    public List<Entity> extractAddedEntities(Interaction interaction1, Interaction interaction2) {

        List<Entity> addedEntities = new ArrayList<>();

        Entity[] Eq1 = interaction1.getEntities();
        Entity[] Eq2 = interaction2.getEntities();

        /* get the added entities */

        for (Entity e2 : Eq2) {
            boolean correctEntity = true;

            for (Entity e1 : Eq1) {
                if (e1.getMention().equals(e2.getMention())) {
                    correctEntity = false;
                    break;
                }
            }

            if (correctEntity) {
                addedEntities.add(e2);
            }
        }

        return addedEntities;

    }

    public List<Entity> extractRemovedEntities(Interaction interaction1, Interaction interaction2) {


        List<Entity> removedEntities = new ArrayList<>();

        Entity[] Eq1 = interaction1.getEntities();
        Entity[] Eq2 = interaction2.getEntities();

        /* get the removed entities */

        for (Entity e1 : Eq1) {
            boolean correctEntity = true;

            for (Entity e2 : Eq2) {
                if (e1.getMention().equals(e2.getMention())) {
                    correctEntity = false;
                    break;
                }
            }

            if (correctEntity) {
                removedEntities.add(e1);
            }
        }

        return removedEntities;

    }

}
