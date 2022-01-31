package lookThenLeap;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LookThenLeapRule {
    private static class Secretary {
        int score;
        int index;
        public Secretary(int index, int score) {
            this.score = score;
            this.index = index;
        }

        @Override
        public String toString() {
            return "Secretary{" +
                    "score=" + score +
                    ", index=" + index +
                    '}';
        }
    }

    public static List<Secretary> generateRandomSecretaryList(int size) {
        List<Secretary> secretaryList = new ArrayList<>();
        for (int counter = 0; counter < size; counter++) {
            secretaryList.add(new Secretary(counter, ThreadLocalRandom.current().nextInt(10000)));
        }
        return secretaryList;
    }

    public static Secretary bestSecretaryInTheList(List<Secretary> secretaryList) {
        int secretaryIndexCounter = 1;
        Secretary bestSecretary = secretaryList.get(0);
        while (secretaryIndexCounter < secretaryList.size()) {
            Secretary secretaryAtIndex = secretaryList.get(secretaryIndexCounter);
            if (secretaryAtIndex.score >= bestSecretary.score) {
                bestSecretary = secretaryAtIndex;

            }
            secretaryIndexCounter ++;
        }
        return bestSecretary;
    }

    public static Secretary lookPhase(List<Secretary> secretaryList, int lookPhaseCounter) {
        Secretary bestSecretaryInLookStep = secretaryList.get(0);
        int secretaryIndexCounter = 1;
        while (secretaryIndexCounter < lookPhaseCounter) {
            Secretary secretaryAtIndex = secretaryList.get(secretaryIndexCounter);
            if (secretaryAtIndex.score >= bestSecretaryInLookStep.score) {
                bestSecretaryInLookStep = secretaryAtIndex;
            }
            secretaryIndexCounter ++;
        }
        return bestSecretaryInLookStep;
    }

    public static int leapPhase(List<Secretary> secretaryList,  Secretary bestSecretaryInLookPhase, Secretary bestSecretaryAmongAllApplicants, int lookPhaseCounter) {
        List<Secretary> afterLookPhaseApplicants = secretaryList.subList(lookPhaseCounter , secretaryList.size());

        for (Secretary secretary : afterLookPhaseApplicants) {
            if (secretary.score < bestSecretaryInLookPhase.score) {
                // Next applicant after the 37th applicant(index=36th, if you count from zero!!) whose score is not higher than we saw in the loop phase
                // so continue with the next applicant
                continue;
            }
            // Next applicant's score is higher than we saw in the loop phase
            // We will choose the next applicant immediately because his/her score is higher than we saw in the loop phase

            if (secretary.score >= bestSecretaryAmongAllApplicants.score) {
                // Next applicant's score is also higher than or equal to the all applicants
                // Therefore our look-then-leap strategy really worked !!
                // we have found the best applicant with 37% chance
                return 1;
            }
            break;
        }
        // Unfortunately, even we chose the next applicant (we are sure that her/his score is higher than in the loop phase)
        // However, she/he was not the best applicant in the applicant pool.
        // That's means out look-and-leap strategy failed.
        return 0;
    }

    public static void runLookAndLeapRule(int totalApplicants, int lookPhaseCounter) {
        double totalTrial = 10;
        double trialUpperLimit = 1000000;
        double multiplyFactor = 10;

        while (totalTrial <= trialUpperLimit) {

            double successfullyFound = 0;
            for (int trial = 0; trial < totalTrial; trial++) {
                List<Secretary> randomList = generateRandomSecretaryList(totalApplicants);
                Secretary bestSecretaryAmongAllApplicants = bestSecretaryInTheList(randomList);

                Secretary bestSecretaryInLookPhase = lookPhase(randomList, lookPhaseCounter);
                int leapPhase = leapPhase(randomList, bestSecretaryInLookPhase, bestSecretaryAmongAllApplicants, lookPhaseCounter);
                successfullyFound += leapPhase;
            }

            System.out.println("---------");
            System.out.println("After number of '" + totalTrial + "' trials, number of '" + successfullyFound + "' times we found best applicant among all applicants(" + totalApplicants + ")");
            System.out.println("Success rate to find best secretary: " + (successfullyFound*100/totalTrial));
            System.out.println("");


            totalTrial *= multiplyFactor;
        }
    }

    public static void main(String[] args) {
        int totalApplicants = 100; // any number except 1

        int lookPhaseCounter = (int) Math.round(totalApplicants / Math.E);

        System.out.println("There are " + totalApplicants + " applicants applied our job and We will look at the first: " + lookPhaseCounter + " applicants.");
        System.out.println("We will find the best applicant among the first: " + lookPhaseCounter + " applicants.");
        System.out.println("After the first: " + lookPhaseCounter + " applicants, We will immediately choose the one whose score is higher than from the first: " + lookPhaseCounter + " applicants.");
        System.out.println();

        runLookAndLeapRule(totalApplicants, lookPhaseCounter);

    }
}
