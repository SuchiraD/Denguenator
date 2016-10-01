package jaegers.denguenator.metapopmodel; /**
 * Created by Janitha on 6/28/2016.
 */

import jaegers.denguenator.GraphGenerator.DrawGraph;
import jaegers.denguenator.GraphGenerator.XYLineChartExample;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import javax.swing.*;
import java.io.FileReader;
import java.util.Arrays;

public class MetaPopulationModel {

    public static int currentSvi;
    public static int currentEvi;
    public static int currentIvi;
    public static int currentShi;
    public static int currentEhi;
    public static int currentIhi;
    public static CSVReader reader;
    public static CSVWriter writer;
    private static Double[][] mobilityBetweenMOH;
    private static int mohAreasCount = 322;
    private static int[] homePopulation;
    private static int[] workPopulation;
    private static int[][] infected; // infected[mohID][weekNo]
    private static double muVi = 1 / 10.5d;
    private static double muHi = 0.0001d;   // What is the Host birth death ratio? // assume the entire population in the patch is susceptible ignore Mhi 16.24/6.06 used
    private static double betaVi = 0.25d; // Ranging from 0.2-0.4
    private static double betaHi = 0.63d;  // Ranging from 0.25 - 0.45
    private static double kappa = 1 / 5.5d;
    private static double lambda = 1 / 5.5d;
//    private static double delta = 20d;
    private static double delta = 0.25d;
    private static int scalingFactor = 10;
    private static int mohSamplePop = 577927;
    private static double mohSampleMob = 3981;
    private static double mohSampleCon = 0.1;
//    private static double mohSampleConWithinRgn = 0.0001;

    public static void main(String[] args) {
        Double rateOfSvi;
        Double rateOfEvi;
        Double rateOfIvi;
        Double rateOfShi;
        Double rateOfEhi;
        Double rateOfIhi;
        Double weeklyRateOfIhi;
        Double Ihi;
        int nextSvi;
        int nextEvi;
        int nextIvi;
        int nextShi;
        int nextEhi;
        int nextIhi;
        int week = 0;
        final int id = 69;
        String[] line;
        final int[] infectedPredictions = new int[53];
        double vectorPopulation; // 3times the Host population in the area
//        initializeMobility();
        int val = 0;
        double error = 1000;

//        for(int e = 3500; e < 4500; e++) {
//            mohSampleMob = e;

//            System.out.println("Mobility Initialized");
            initializePopulation();
//            System.out.println("Population Initialized");
            initializeInfected();
//            System.out.println("Infected Initialized");
            setInitialConditions(id, 0);
//            System.out.println("Initial conditions created");
//            System.out.println("betaVi,betaHi,scalingFactor = " + betaVi + "," + betaHi + "," + scalingFactor);
            infectedPredictions[0] = currentIhi;
//        try {
//            writer = new CSVWriter(new FileWriter("/Users/Anushka/Documents/workspace/CSV/Colombo_MSE.csv"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        for (int bvi = 0; bvi<100 ; bvi++) {
//            betaVi = (bvi * 0.01);
//            for (int bhi = 0; bhi < 100; bhi++) {
//                betaHi = (bhi * 0.01);
            for (int i = 1; i <= 52; i++) {
                week = i;
                weeklyRateOfIhi = 0.0;

                setInitialConditions(69, week - 1);

                for (int j = 0; j < 7; j++) {   // Do this for a week as we calculate daily figures.
                    //Equations for the vectors
                    rateOfSvi = muVi * scalingFactor * getMOHPopulation(id) - betaVi * getConnectivity(id, week) * currentSvi - muVi * currentSvi;  // scalingFactor*HostPopulation = vectorPopulation
                    rateOfEvi = betaVi * getConnectivity(id, week) * currentSvi - muVi * currentEvi - kappa * currentEvi;
                    rateOfIvi = kappa * currentEvi - muVi * currentIvi;

                    // Equations for the hosts
                    rateOfShi = getMOHPopulation(id) - betaHi * getConnectivityWithinRegion(id) * currentShi - muHi * currentShi;
                    rateOfEhi = betaHi * getConnectivityWithinRegion(id) * currentShi - lambda * currentEhi - muHi * currentEhi;
                    rateOfIhi = lambda * currentEhi - delta * currentIhi - muHi * currentIhi;
                    weeklyRateOfIhi += rateOfIhi;
                    // Obtain next states

//                if (i == 0) {
//                    currentSvi = rateOfSvi.intValue();
//                    currentEvi = rateOfEvi.intValue();
//                    currentIvi = rateOfIvi.intValue();
//                    currentShi = rateOfShi.intValue();
//                    currentEhi = rateOfEhi.intValue();
//                    currentIhi = rateOfIhi.intValue();
//                }else{
                    currentSvi += rateOfSvi.intValue();
                    currentEvi += rateOfEvi.intValue();
                    currentIvi += rateOfIvi.intValue();
                    currentShi += rateOfShi.intValue();
                    currentEhi += rateOfEhi.intValue();

//                    if (rateOfIhi.intValue() > 0) {
//                        currentIhi += rateOfIhi.intValue();
//                    }
//                }


                }
                infectedPredictions[week] = weeklyRateOfIhi.intValue();
                //System.out.println("Week " + week + " Prediction " + infectedPredictions[week] + " Actual " + infected[id][week]);
                //System.out.print(infectedPredictions[week] + ",");
                //System.out.print(infected[id][week] + ",");
                // Set current infected to actual value for next prediction
                currentIhi = infected[id][week];
            }
            System.out.println(Arrays.toString(infectedPredictions));
            System.out.println(Arrays.toString(infected[id]));

//            double er = calculateMSE(infectedPredictions, infected[id]);
            System.out.println(betaVi + " " + betaHi + " " + calculateMSE(infectedPredictions, infected[id]));

//            if(er < error){
//                error = er;
//                val = e;
//            }

//        DrawGraph provider = new DrawGraph();
//        DrawGraph.createAndShowGui();
            int[] actual = Arrays.copyOfRange(infected[id], 0, 52);
            int[] predict = Arrays.copyOfRange(infectedPredictions, 0, 52);

//            System.out.println();
//        }

//        System.out.println(val);
//        System.out.println(Arrays.toString(predict));
//        System.out.println(Arrays.toString(actual));

        XYLineChartExample temp = new XYLineChartExample(actual, predict);


//        BufferedWriter br = null;
//        try {
//            br = new BufferedWriter(new FileWriter("myfile.csv"));
//
//            StringBuilder sb = new StringBuilder();
//            for (int element : infectedPredictions) {
//                sb.append(element);
//                sb.append(",");
//            }
//            sb.append(",");
//
//            for (int element : infected[id]) {
//                sb.append(element);
//                sb.append(",");
//            }
//
//            br.write(String.valueOf(sb));
//            br.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        //line = new String[]{Double.toString(betaVi), Double.toString(betaHi), Double.toString(calculateMSE(infectedPredictions, infected[id]))};
        //writer.writeNext(line);
//            }
//
//        }
//        try {
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private static void setInitialConditions(int id, int week) {
        // initial conditions
//        int week = 0;
        // for the week 1, value(t+1) =  1 * rateOfChange + value(t) ; assume value(t) = 0
        // then value(t+1) = rateOfChange
        currentSvi = (int) (muVi * scalingFactor * getMOHPopulation(id));
        currentEvi = (int) (betaVi * getConnectivity(id, week) * currentSvi);
        currentIvi = (int) (kappa * currentEvi);

        currentShi = (int) (getMOHPopulation(id)); // muhi * mohpopulation
//        currentEhi = (int) 100;
//        currentEhi = (int) ((betaHi/mohSamplePop) * (currentIvi / (3 * (double) mohSamplePop)) * currentShi);
//        currentEhi = (int) ((betaHi/getMOHPopulation(id) /*homePopulation[id]*/) * (currentIvi / (3 * (double) getMOHPopulation(id))) * currentShi);
//        currentEhi = (int) ((betaHi * mobilityBetweenMOH[id][id] / homePopulation[id]) * (currentIvi / (3 * (double) getMOHPopulation(id))) * currentShi);
//        currentEhi = (int) ((betaHi * mohSampleMob / mohSamplePop) * (currentIvi / (3 * (double) mohSamplePop)) * currentShi);
        currentEhi = (int) (betaHi * getConnectivityWithinRegion(id) * currentShi);
        currentIhi = infected[id][week]; // muHi * infected
    }

    private static void initializeInfected() {
        infected = new int[mohAreasCount][53]; // 322 MOH areas and (52 weeks + the total number of cases = 53)
        String[] nextline;
        int mohID;
        try {
            reader = new CSVReader(new FileReader("D:/Final Year Project/Data/Dengue/dengueCases2013.csv"));
//            reader = new CSVReader(new FileReader("/Users/Anushka/Documents/workspace/CSV/dengueCases.csv"));

        } catch (Exception e) {
            System.out.println(e);
        }
        try {

            while ((nextline = reader.readNext()) != null) {
                mohID = Integer.parseInt(nextline[0]);
                for (int i = 2; i < nextline.length; i++) {  // Dengue cases data starts from 3rd row of the file
                    infected[mohID][i - 2] = Integer.parseInt(nextline[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initializePopulation() {
        String[] nextline;
        homePopulation = new int[mohAreasCount];
        workPopulation = new int[mohAreasCount];
        try {
//            reader = new CSVReader(new FileReader("/Users/Anushka/Documents/workspace/CSV/resident_work_processed.csv"));

        } catch (Exception e) {
            System.out.println(e);
        }
        try {
//            while ((nextline = reader.readNext()) != null) {
//                homePopulation[Integer.parseInt(nextline[0])] = Integer.parseInt(nextline[2]);
//                workPopulation[Integer.parseInt(nextline[0])] = Integer.parseInt(nextline[3]);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void initializeMobility() {
        String[] nextline;
        mobilityBetweenMOH = new Double[mohAreasCount][mohAreasCount];
        try {
//            reader = new CSVReader(new FileReader("/Users/Anushka/Documents/workspace/CSV/results.csv"));

        } catch (Exception e) {
            System.out.println(e);
        }
        try {
//            while ((nextline = reader.readNext()) != null) {
////                if (nextline[0].equals("source")) {
////                    continue;
////                }
////                int i = Integer.parseInt(nextline[0]);        //get the source
////                int j = Integer.parseInt(nextline[1]);        //get the destination
////
////                mobilityBetweenMOH[i][j] = Double.parseDouble(nextline[2]);      //put the value to the array
//
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getMOHPopulation(int id) {
        int population = homePopulation[id]; // for now we use residents in each moh as the population
        // Get the averaged population
//        return population;
       return mohSamplePop;
    }

    public static double getConnectivity(int source, int week) {
        // get dengue-conecctivity not only mobility
//        double connectivity = 0;
//        int i = 0;
//        for (i = 0; i < mohAreasCount; i++) {
//            connectivity += (mobilityBetweenMOH[source][i] / getMOHPopulation(source)) * (infected[i][week] / (double) homePopulation[i]);
//        }
//        if (connectivity > 1) {
//            connectivity = 1;
//        }
//        return connectivity;

        return  mohSampleCon;
//        return  .00001;
    }

    public static double getConnectivityWithinRegion(int id) {
        // get dengue-conecctivity not only mobility
//        double connectivity = (mobilityBetweenMOH[id][id] / getMOHPopulation(id)) * (currentIvi / (3 * (double) getMOHPopulation(id)));  // 3*mohPopulation = N(vi)
//        if (connectivity > 1) {
//            connectivity = 1;
//        }
//        return connectivity;

        double connectivity = (mohSampleMob / getMOHPopulation(id)) * (currentIvi / (3 * (double) getMOHPopulation(id)));  // 3*mohPopulation = N(vi)
        if (connectivity > 1) {
            connectivity = 1;
            System.out.println("awa");
        }
        return connectivity;

//        return .00001;
//        return mohSampleConWithinRgn;
    }

    public static int getNextState(double rate, int currentState, int week) {
        return (int) (rate * week + currentState);
    }

    public static double calculateMSE(int predictions[], int actual[]) {
        double MSE = 0.0;
        double sumOfSquares = 0.0;
        for (int i = 0; i < 52; i++) {
            sumOfSquares += Math.pow(actual[i] - predictions[i], 2);
        }
        MSE = Math.sqrt(sumOfSquares / 52);
        return MSE;
    }

}

