package denguenatorone;

import static jaegers.denguenator.entoepidmodel.EntoEpidMain.validate;

public class Metapop {
    public static double gammaH = 0.5;
    public static double sigmaH = 0.25;

    public static void main(String[] args) {
        int MAX_DAYS = 30;
        int Nh = 200000;

        double[] Sh = new double[MAX_DAYS + 2];
        double[] Eh = new double[MAX_DAYS + 2];
        double[] Ih = new double[MAX_DAYS + 2];
        double[] Rh = new double[MAX_DAYS + 2];
        double[] Sv = new double[MAX_DAYS + 2];
        double[] Ev = new double[MAX_DAYS + 2];
        double[] Iv = new double[MAX_DAYS + 2];

        double[] V = new double[MAX_DAYS + 2];

        double[] N = new double[MAX_DAYS + 2];

        initialise(V, Sv, Ev, Iv);
        Sh[1] = Nh;

        ode(Sh, Eh, Ih, Rh, Sv, Ev, Iv, V, Nh, N, MAX_DAYS);

        System.out.print("Sh = ");
        print(Sh);
        System.out.print("Eh = ");
        print(Eh);
        System.out.print("Ih = ");
        print(Ih);
        System.out.print("Rh = ");
        print(Rh);
        System.out.print("V = ");
        print(V);
        System.out.print("N = ");
        print(N);
    }

    private static void print(double[] array) {
        for (double d : array) {
            System.out.print(d + "  , ");
        }
        System.out.println();
    }

    private static void ode(double[] Sh, double[] Eh, double[] Ih, double[] Rh,
            double[] Sv, double[] Ev, double[] Iv,
            double[] V, int Nh, double[] N, int MAX_DAYS) {

        double[] dShArray = new double[MAX_DAYS + 2];
        double[] dEhArray = new double[MAX_DAYS + 2];
        double[] dIhArray = new double[MAX_DAYS + 2];
        double[] dRhArray = new double[MAX_DAYS + 2];
        double[] dSvArray = new double[MAX_DAYS + 2];
        double[] dEvArray = new double[MAX_DAYS + 2];
        double[] dIvArray = new double[MAX_DAYS + 2];
        double[] dAArray = new double[MAX_DAYS + 2];


        for(int i = 1; i <= MAX_DAYS; i++) {
            gammaH = 0.1818;

            long dSh = (long) (-0.63 * Iv[i] * Sh[i] / (3*V[i]));
            long dEh = (long) (0.63 * Iv[i] * Sh[i] / (3*V[i]) - gammaH * Eh[i]);
            long dIh = (long) (gammaH * Eh[i] - sigmaH * Ih[i]);
            long dRh = (long) (sigmaH * Ih[i]);

            long dSv = (long) (0.0952 * V[i] - 0.25 * Ih[i] * Sv[i] / Nh - 0.0952 * Sv[i]);
            long dEv = (long) (0.25 * Ih[i] * Sv[i] / Nh - 0.1818 * Ev[i] - 0.0952 * Ev[i]);
            long dIv = (long) (0.1818 * Ev[i] - 0.0952 * Iv[i]);

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
        System.out.print("dSv = ");
        print(dSvArray);
        System.out.print("dIv = ");
        print(dIvArray);
        System.out.println();
    }

    public static void initialise(double[] V, double[] Sv, double[] Ev, double[] Iv) {
        V[1] = 3*200000;

        double n = V[1] / 2;
        Iv[1] = (long) (n * 0.98);
        Sv[1] = (long) n;
        Ev[1] = V[1] - (Sv[1] + Iv[1]);

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

}
