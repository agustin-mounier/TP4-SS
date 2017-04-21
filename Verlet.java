import com.sun.tools.javac.util.Pair;

public class Verlet {

    private OscillatorForce f;
    private double dT;

    private double prevX;
    private double prevY;

    private double prevVelX;
    private double prevVelY;

    private Particle particle;

    public Verlet(OscillatorForce f, double dT) {
        this.f = f;
        this.dT = dT;
        this.particle = f.getParticle();
        runFirstIteration();
    }

    public ParticleState moveParticle() {
        double newX = 2 * particle.x - prevX + (((dT * dT) * f.getXForce()) / particle.mass);
        double newY = 2 * particle.y - prevY + (((dT * dT) * f.getYForce()) / particle.mass);

        double newVelX = (newX - prevX) / (2 * dT);
        double newVelY = (newY - prevY) / (2 * dT);

        prevX = particle.x;
        prevY = particle.y;

        prevVelX = particle.velX;
        prevVelY = particle.velY;

        particle.x = newX;
        particle.y = newY;
        particle.velX = newVelX;
        particle.velY = newVelY;

        return new ParticleState(newX, newY, newVelX, newVelY);
    }

    private void runFirstIteration() {
        prevX = particle.x;
        prevY = particle.y;

        prevVelX = particle.velX;
        prevVelY = particle.velY;

        double newX = particle.x + (dT * particle.velX) + (((dT * dT) * f.getXForce()) / (2 * particle.mass));

        double newVelX = particle.velX - ((dT * f.getXForce()) / particle.mass);

        particle.x = newX;
        particle.y = 0;
        particle.velX = newVelX;
        particle.velY = 0;
    }
}