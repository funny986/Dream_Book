package com.dreambook;

import androidx.transition.*;

public class DetailsTransition extends TransitionSet {

    public DetailsTransition() {
        ChangeBounds bounds = new ChangeBounds();
//                bounds.captureEndValues(TransitionValues.class.);

        setOrdering(ORDERING_TOGETHER);
                addTransition(new ChangeBounds()).
                addTransition(new ChangeTransform()).
                addTransition(new ChangeImageTransform());
    }

}