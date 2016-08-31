package jaegers.denguenator.entoepidmodel;

import jaegers.denguenator.csvreader.ReadDengueCases;

import java.util.List;
import java.util.Random;

/**
 * Created by suchira on 7/29/16.
 */
public class EntoEpidMain {
    public static double K1 = Math.pow(10, 10);
    public static double K = 10;
    public static double a = 0.4;
    public static double gammaH = 0.5;
    public static double sigmaH = 0.25;
    public static List<Integer> dengueList = null;

    public static void main(String[] args) {
        int MAX_DAYS = 365;
        int Nh = 200000;

        double[] temperature = new double[MAX_DAYS + 2];    //Start from temperature[1]
        double temp = 28;

        double[] Sh = new double[MAX_DAYS + 2];
        double[] Eh = new double[MAX_DAYS + 2];
        double[] Ih = new double[MAX_DAYS + 2];
        double[] Rh = new double[MAX_DAYS + 2];
        double[] Sv = new double[MAX_DAYS + 2];
        double[] Ev = new double[MAX_DAYS + 2];
        double[] Iv = new double[MAX_DAYS + 2];

        double[] A = new double[MAX_DAYS + 2];
        double[] V = new double[MAX_DAYS + 2];

        double[] N = new double[MAX_DAYS + 2];

        initialise(A, V, Sv, Ev, Iv, temperature, temp);
        Sh[1] = Nh;

        ode(Sh, Eh, Ih, Rh, Sv, Ev, Iv, A, V, Nh, N, temperature, MAX_DAYS);

        System.out.println("\nShiHV: " + shiHV(temp));
        System.out.println("ShiVH: " + shiVH(temp));
        System.out.println("thetaA: " + thetaA(temp));
        System.out.println("epsilonAV: " + epsilonAV(temp));
        System.out.println("muAV: " + muAV(temp));
        System.out.println("muVV: " + muVV(temp));
        System.out.println("gammaVV: " + gammaVV(temp));
        System.out.println("basic reproduction: " + q(5));
        System.out.println("\n");

        System.out.print("Sh = ");
        print(Sh);
        System.out.print("Eh = ");
        print(Eh);
        System.out.print("Ih = ");
        print(Ih);
        System.out.print("Rh = ");
        print(Rh);
        System.out.print("A = ");
        print(A);
        System.out.print("V = ");
        print(V);
        System.out.print("N = ");
        print(N);
        System.out.print("T = ");
        print(temperature);

        System.out.print("Total infected people: ");
        double sum = 0;
        for (double d : Ih) {
            sum += d;
        }
        System.out.println(sum);

    }

    private static void print(double[] array) {
        for (double d : array) {
            System.out.print(d + "  , ");
        }
        System.out.println();
    }

    private static void ode(double[] Sh, double[] Eh, double[] Ih, double[] Rh, double[] Sv, double[] Ev, double[] Iv,
            double[] A, double[] V, int Nh, double[] N, double[] temperature, int MAX_DAYS) {

        double[] dShArray = new double[MAX_DAYS + 2];
        double[] dEhArray = new double[MAX_DAYS + 2];
        double[] dIhArray = new double[MAX_DAYS + 2];
        double[] dRhArray = new double[MAX_DAYS + 2];
        double[] dSvArray = new double[MAX_DAYS + 2];
        double[] dEvArray = new double[MAX_DAYS + 2];
        double[] dIvArray = new double[MAX_DAYS + 2];
        double[] dAArray = new double[MAX_DAYS + 2];


        for(int i = 1; i <= MAX_DAYS; i++) {
            double dSh = (long) (-a * shiVH(temperature[i]) * Iv[i] * Sh[i] / Nh);
            double dEh = (long) (a * shiVH(temperature[i]) * Iv[i] * Sh[i] / Nh - gammaH * Eh[i]);
            double dIh = (long) (gammaH * Eh[i] - sigmaH * Ih[i]);
            double dRh = (long) (sigmaH * Ih[i]);

            double dA;
            /*if(A[i] < K )
                dA = (long) (thetaA(temperature[i]) * (1 - A[i] / K) * V[i] - (epsilonAV(temperature[i]) + muAV(temperature[i])) * A[i]);
            else
                dA = - (epsilonAV(temperature[i]) + muAV(temperature[i])) * A[i];*/

//            double dSv = (long) (epsilonAV(temperature[i]) * A[i] - a * shiHV(temperature[i]) * Ih[i] * Sv[i] / Nh - muVV(temperature[i]) * Sv[i]);
            double dSv = (long) (muVV(temperature[i]) * V[i] - a * shiHV(temperature[i]) * Ih[i] * Sv[i] / Nh - muVV(temperature[i]) * Sv[i]);
            double dEv = (long) (a * shiHV(temperature[i]) * Ih[i] * Sv[i] / Nh - gammaVV(temperature[i]) * Ev[i] - muVV(temperature[i]) * Ev[i]);
            double dIv = (long) (gammaVV(temperature[i]) * Ev[i] - muVV(temperature[i]) * Iv[i]);

            /**********************************************/
            dShArray[i] = dSh;
            dEhArray[i] = dEh;
            dIhArray[i] = dIh;
            dRhArray[i] = dRh;
            dSvArray[i] = dSv;
            dEvArray[i] = dEv;
            dIvArray[i] = dIv;
            /**********************************************/

            Sh[i+1] = validate(Sh[i] + dSh);
            Eh[i+1] = validate(Eh[i] + dEh);
            Ih[i+1] = validate(Ih[i] + dIh);
            Rh[i+1] = validate(Rh[i] + dRh);

            Sv[i+1] = validate(Sv[i] + dSv);
            Ev[i+1] = validate(Ev[i] + dEv);
            Iv[i+1] = validate(Iv[i] + dIv);

//            A[i+1] = validate(A[i] + dA);
            V[i+1] = validate(Sv[i+1] + Ev[i+1] + Iv[i+1]);

            N[i+1] = Sh[i+1] + Eh[i+1] + Ih[i+1] + Rh[i+1];

        }

        System.out.print("dSh = ");
        print(dShArray);
        System.out.print("dIh = ");
        print(dIhArray);
        System.out.print("dSv = ");
        print(dSvArray);
        System.out.print("dIv = ");
        print(dIvArray);
        System.out.print("dA = ");
        print(dAArray);
        System.out.print("dSv = ");
        print(dSvArray);
        System.out.print("dIv = ");
        print(dIvArray);
    }

    public static void initialise(double[] A, double[] V, double[] Sv, double[] Ev, double[] Iv, double[] temperature, double temp) {
        ReadDengueCases dengueCasesReader = new ReadDengueCases(
                "/media/suchira/0A9E051F0A9E051F/CSE 2012/Semester 07-08/FYP/Denguenator/Dengunator 2.0/Data/Dengue/dengueCases2013.csv"
        );
        dengueList = dengueCasesReader.getDengueCases("MC - Colombo");

        for (int i=0; i < temperature.length; i++) {
            Random r = new Random();

            temperature[i] = r.doubles(25, 30).iterator().nextDouble();
        }


//        V[1] = (long) v(temperature[1]);
        V[1] = 200000;
        A[1] = (long) a(temperature[1]);

        double n = V[1] / 2;
        //        Sv[1] = Ev[1] = Iv[1] = n;
        Iv[1] = (long) (n * 0.98);
        Sv[1] = (long) n;
        Ev[1] = V[1] - (Sv[1] + Iv[1]);

    }

    public static double validate(double value) {
        if(value < 0) {

            return 0;
        }

        return value;
    }

    public static double rhf(double T) {
        double vp = 0.6108 * Math.exp(17.27 * T / (237.3 + T));
        vp *= 7.500617;
        if (10 < vp && vp < 30) {

            return 12 - 0.2 * vp;
        } else if (vp >= 30)

            return 0.5;

        return 0.5;
    }

    public static double shiHV(double T) {
        if(T > 32.461) {
            return 0;
        }

        double answer = 1.044 * 0.001 * T * (T - 12.286) * Math.sqrt(32.461 - T);
        if(answer < 0)
            return 0;

        return answer;
    }

    public static double shiVH(double T) {
        double answer = 0.0729 * T - 0.97;
        if(answer < 0)
            return 0;
        if(answer > 1)
            return 1;

        return answer;
    }

    public static double thetaA(double T) {
        double answer = -5.4 + 1.8 * T - 0.2124 * Math.pow(T, 2) + 0.01015 * Math.pow(T, 3) - 1.515 * 0.0001 * Math.pow(T, 4);
        if(answer < 0)
            return 0;

        return answer;
    }

    public static double epsilonAV(double T) {
        double answer = 0.131 - 0.05723 * T + 0.01164 * Math.pow(T, 2) - 0.001341 * Math.pow(T, 3) + 0.00008723 * Math.pow(T, 4)
                - 3.017 * Math.pow(10, -6) * Math.pow(T, 5) + 5.153 * Math.pow(10, -8) * Math.pow(T, 6) - 3.42 * Math
                .pow(10, -10) * Math.pow(T, 7);
        if(answer < 0)
            return 0;

        return answer;
    }

    public static double muAV(double T) {
        double answer = 2.13 - 0.3787*T + 0.02457*Math.pow(T,2) - 6.778*Math.pow(10, -4)*Math.pow(T,3) + 6.794*Math.pow(10, -6)*Math.pow(T,4);
        if(answer < 0)
            return 0;

        return answer;
    }

    public static double muVV(double T) {
        double answer = (0.8692 - 0.1599*T + 0.01116*Math.pow(T,2) -3.408*Math.pow(10, -4)*Math.pow(T,3) + 3.809*Math.pow(10, -6)*Math.pow(T, 4));
        if(answer < 0)
            return Math.abs(answer);

        return answer;
    }

    public static double gammaVV(double T) {
        double R = 8.3144598;
        double Tk = 273 + T;
        double answer = (3.3589*0.001*Tk/298) * Math.exp((1500/R)*(1/298-1/Tk)) / (1 + Math.exp((6.203*Math.pow(10, 21)/R)*(1/(-2.176*Math.pow(10, 30)) - 1/Tk)));
        if(answer < 0)
            return 0;

        return answer;
    }

    public static double q(double T) {
        double answer = (epsilonAV(T)/(epsilonAV(T) + muAV(T))) * thetaA(T)/muVV(T);
        /*if(answer < 0)
            return 0;*/

        return answer;
    }

    public static double a(double T) {
        double answer = K * (1 - 1/q(T));
        if(answer < 0)
            return 0;

        return answer;
    }

    public static double v(double T) {
        double answer = K*(1-1/q(T))*epsilonAV(T)/muVV(T);
        if(answer < 0)
            return 0;

        return answer;
    }



    /*public static double r0(double T) {

        return ()
    }*/
}
