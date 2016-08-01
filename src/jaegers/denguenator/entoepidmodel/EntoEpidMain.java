package jaegers.denguenator.entoepidmodel;

/**
 * Created by suchira on 7/29/16.
 */
public class EntoEpidMain {
    public static double K = Math.pow(10, 6);
    public static double a = 0.4;

    public static void main(String[] args) {
        int MAX_DAYS = 5;
        int Nh = 200000;
        double gammaH = 0.5;
        double sigmaH = 0.25;
        double[] temperature = {0, 25, 25, 25, 25, 25, 25};    //Start from temperature[1]
        double temp = 25;

        double[] Sh = {1, 0, 0, 0, 0, 0, 0};
        double[] Eh = {1, 0, 0, 0, 0, 0, 0};
        double[] Ih = {1, 0, 0, 0, 0, 0, 0};
        double[] Rh = {1, 0, 0, 0, 0, 0, 0};
        double[] Sv = {1, 0, 0, 0, 0, 0, 0};
        double[] Ev = {1, 0, 0, 0, 0, 0, 0};
        double[] Iv = {1, 0, 0, 0, 0, 0, 0};

        double[] A = {1, 0, 0, 0, 0, 0, 0};
        double[] V = {1, 0, 0, 0, 0, 0, 0};

        initialise(A, V, Sv, Ev, Iv, temperature[0]);
        Sh[1] = Nh;
        for(int i = 1; i <= MAX_DAYS; i++) {
            double dSh = -a * shiVH(temperature[i]) * Iv[i] * Sh[i] / Nh;
//            System.out.println("betaVH = " + a * shiVH(temperature[i]));
            double dEh = a * shiVH(temperature[i]) * Iv[i] * Sh[i] / Nh - gammaH * Eh[i];
            double dIh = gammaH * Eh[i] - sigmaH * Ih[i];
            double dRh = sigmaH * Ih[i];

            double dA = thetaA(temperature[i]) * (1 - A[i] / K) * V[i] - (epsilonAV(temperature[i]) + muAV(temperature[i])) * A[i];
            double dSv = epsilonAV(temperature[i]) * A[i] - a * shiHV(temperature[i]) * Ih[i] * Sv[i] / Nh - muVV(temperature[i]) * Sv[i];
//            System.out.println("betaHV = " + a * shiHV(temperature[i]));
            System.out.println(a * shiHV(temperature[i]) * Ih[i] * Sv[i] / Nh);
            double dEv = a * shiHV(temperature[i]) * Ih[i] * Sv[i] / Nh - gammaVV(temperature[i]) * Ev[i] - muVV(temperature[i]) * Ev[i];
            double dIv = gammaVV(temperature[i]) * Ev[i] - muVV(temperature[i]) * Iv[i];

            Sh[i+1] = Sh[i] + dSh;
            Eh[i+1] = Eh[i] + dEh;
            Ih[i+1] = Ih[i] + dIh;
            Rh[i+1] = Rh[i] + dRh;

            Sv[i+1] = Sv[i] + dSv;
            Ev[i+1] = Ev[i] + dEv;
            Iv[i+1] = Iv[i] + 0;

            A[i+1] = A[i] + dA;
            V[i+1] = Sv[i+1] + Ev[i+1] + Iv[i+1];
        }

        System.out.print("Sh = ");
        for (double d : Sh) {
            System.out.print(d + ", ");
        }
        System.out.print("\nIh = ");
        for (double d : Ih) {
            System.out.print(d + ", ");
        }
    }

    public static double rhf(double T) {
        //        double vp = 6.11*Math.pow(10, (7.5*T/(237.3+T)/10)); // This is what was in the Pakistan paper and it doesn't work
        double vp = 0.6108 * Math.exp(17.27 * T / (237.3 + T));
        vp *= 7.500617;
        if (10 < vp && vp < 30) {

            return 1.2 - 0.2 * vp;
        } else if (vp >= 30)

            return 0.5;

        return vp;
    }

    public static double shiHV(double T) {

        return 1.044 * 0.001 * T * (T - 12.286) * Math.sqrt((32.461 - T));
    }

    public static double shiVH(double T) {

        return 0.0729 * T - 0.97;
    }

    public static double thetaA(double T) {

        return -5.4 + 1.8 * T - 0.2124 * Math.pow(T, 2) + 0.01015 * Math.pow(T, 3) - 1.515 * 0.0001 * Math.pow(T, 4);
    }

    public static double epsilonAV(double T) {

        return 0.131 - 0.05723 * T + 0.01164 * Math.pow(T, 2) - 0.001341 * Math.pow(T, 3) + 0.00008723 * Math.pow(T, 4)
                - 3.017 * Math.pow(10, -6) * Math.pow(T, 5) + 5.153 * Math.pow(10, -8) * Math.pow(T, 6) - 3.42 * Math
                .pow(10, -10) * Math.pow(T, 7);
    }

    public static double muAV(double T) {

        return 2.13 - 0.3787*T + 0.02457*Math.pow(T,2) - 6.778*Math.pow(10, -4)*Math.pow(T,3) + 6.794*Math.pow(10, -6)*Math.pow(T,4);
    }

    public static double muVV(double T) {

        return rhf(T)*(0.8692 - 0.1599*T + 0.01116*Math.pow(T,2) -3.408*Math.pow(10, -4)*Math.pow(T,3) + 3.809*Math.pow(10, -6)*Math.pow(T, 4));
    }

    public static double gammaVV(double T) {
        double R = 8.3144598 * Math.pow(10, -5);
        double Tk = 273 + T;

        return (3.3589*0.001*Tk/298) * Math.exp((1500/R)*(1/298-1/Tk)) / (1 + Math.exp((6.203*Math.pow(10, 21))/R*(1/(-2.176*Math.pow(10, 30)) - 1/Tk)));
    }

    public static double q(double T) {

        return epsilonAV(T)/(epsilonAV(T) + muAV(T)) + thetaA(T)/muVV(T);
    }

    public static double a(double T) {

        return K * (1 - 1/q(T));
    }

    public static double v(double T) {

        return K*(1-1/a(T))*epsilonAV(T)/muVV(T);
    }

    public static void initialise(double[] A, double[] V, double[] Sv, double[] Ev, double[] Iv, double temperature) {
        V[1] = v(temperature);
        A[1] = a(temperature);

        double n = V[1] / 3;
        Sv[1] = Ev[1] = Iv[1] = n;
    }

    /*public static double r0(double T) {

        return ()
    }*/
}
