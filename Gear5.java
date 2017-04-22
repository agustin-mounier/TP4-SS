/**
 * Created by amounier on 4/21/17.
 */
public class Gear5 {

    private double dT;
    private OscillatorForce f;
    private Particle particle;

    private double[] predictionsX = new double[6];
    private double[] predictionsY = new double[6];
    private double[] factorial = {0.0, 1.0, 2.0, 6.0, 24.0, 120.0};
    private double[] alpha = {(3.0 / 20), (251.0 / 260), 1.0, (11.0 / 18), (1.0 / 6), (1.0 / 60)};


    public Gear5(OscillatorForce f, double dT) {
        this.dT = dT;
        this.f = f;
        this.particle = f.getParticle();
        initialize();
    }


    public ParticleState moveParticle() {

        predict();

        particle.x = predictionsX[0];
        particle.y = predictionsY[0];
        particle.velX = predictionsX[1];
        particle.velY = predictionsY[1];

        double aX = f.getXForce() / particle.mass;
        double aY = f.getYForce() / particle.mass;
        double dR2X = ((aX - predictionsX[2]) * (dT * dT)) / factorial[2];
        double dR2Y = ((aY - predictionsY[2]) * (dT * dT)) / factorial[2];

        correct(dR2X, dR2Y);

        particle.x = predictionsX[0];
        particle.y = predictionsY[0];
        particle.velX = predictionsX[1];
        particle.velY = predictionsY[1];

        return new ParticleState(particle.x, particle.y, particle.velX, particle.velY);
    }


    private void initialize() {
        double K = f.getK();
        double G = f.getG();

        predictionsX[0] = particle.x;
        predictionsX[1] = particle.velX;
        predictionsX[2] = f.getXForce() / particle.mass;
        predictionsX[3] = -(K / particle.mass) * predictionsX[1] - (G / particle.mass) * predictionsX[2];
        predictionsX[4] = -(K / particle.mass) * predictionsX[2] - (G / particle.mass) * predictionsX[3];
        predictionsX[5] = -(K / particle.mass) * predictionsX[3] - (G / particle.mass) * predictionsX[4];

        predictionsY[0] = particle.y;
        predictionsY[1] = particle.velY;
        predictionsY[2] = f.getYForce() / particle.mass;
        predictionsY[3] = -(K / particle.mass) * predictionsY[1] - (G / particle.mass) * predictionsY[2];
        predictionsY[4] = -(K / particle.mass) * predictionsY[2] - (G / particle.mass) * predictionsY[3];
        predictionsY[5] = -(K / particle.mass) * predictionsY[3] - (G / particle.mass) * predictionsY[4];
    }

    private void predict() {
        // X values predictions.
        predictionsX[0] = predictionsX[0] + predictionsX[1] * dT + predictionsX[2] * (dT * dT) / factorial[2] +
                predictionsX[3] * (dT * dT * dT) / factorial[3] + predictionsX[4] * Math.pow(dT, 4) * factorial[4] +
                predictionsX[5] * Math.pow(dT, 5) * factorial[5];

        predictionsX[1] = predictionsX[1] + predictionsX[2] * dT + predictionsX[3] * (dT * dT) / factorial[2] +
                predictionsX[4] * (dT * dT * dT) / factorial[3] + predictionsX[5] * Math.pow(dT, 4) / factorial[4];

        predictionsX[2] = predictionsX[2] + predictionsX[3] * dT + predictionsX[4] * (dT * dT) / factorial[2] +
                predictionsX[5] * (dT * dT * dT) / factorial[3];

        predictionsX[3] = predictionsX[3] + predictionsX[4] * dT + predictionsX[5] * (dT * dT) / factorial[2];
        predictionsX[4] = predictionsX[4] + predictionsX[5] * dT;

        // Y values predictions.
        predictionsY[0] = predictionsY[0] + predictionsY[1] * dT + predictionsY[2] * (dT * dT) / factorial[2] +
                predictionsY[3] * (dT * dT * dT) / factorial[3] + predictionsY[4] * Math.pow(dT, 4) * factorial[4] +
                predictionsY[5] * Math.pow(dT, 5) * factorial[5];

        predictionsY[1] = predictionsY[1] + predictionsY[2] * dT + predictionsY[3] * (dT * dT) / factorial[2] +
                predictionsY[4] * (dT * dT * dT) / factorial[3] + predictionsY[5] * Math.pow(dT, 4) / factorial[4];

        predictionsY[2] = predictionsY[2] + predictionsY[3] * dT + predictionsY[4] * (dT * dT) / factorial[2] +
                predictionsY[5] * (dT * dT * dT) / factorial[3];

        predictionsY[3] = predictionsY[3] + predictionsY[4] * dT + predictionsY[5] * (dT * dT) / factorial[2];
        predictionsY[4] = predictionsY[4] + predictionsY[5] * dT;
    }

    private void correct(double dR2X, double dR2Y) {
        for (int i = 0; i < predictionsX.length; i++) {
            predictionsX[i] = predictionsX[i] + (alpha[i] * dR2X * (factorial[i] / Math.pow(dT, i)));
            predictionsY[i] = predictionsY[i] + (alpha[i] * dR2Y * (factorial[i] / Math.pow(dT, i)));
        }

    }
}
