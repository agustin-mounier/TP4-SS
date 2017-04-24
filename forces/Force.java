package forces;

import models.Particle;

/**
 * Created by sebastian on 4/23/17.
 */
public interface Force {

    double getXForce();
    double getYForce();

    Particle getParticle();


}
