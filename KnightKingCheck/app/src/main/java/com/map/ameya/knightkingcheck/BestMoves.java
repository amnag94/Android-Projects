package com.map.ameya.knightkingcheck;

public class BestMoves
{
    static int[] inputArray;
    static int[] PossibleSolutionIfAllSplitFail;
    int global_solutionCount = 0;
    static int executedonce = 0;
    public static int getSmallestRoute(int knight_x, int knight_y, int king_x, int king_y)
    {
        int least_moves = getStepCount(knight_x, knight_y, king_x, king_y);
        return least_moves;
    }
    public static int checkIfvalid_Count(int[] inputArray1, int sumVal) {
        int[] ArrayOf2s;
        int[] Arrayof1s;
        int CountOf1s = 0, CountOf2s = 0;

        for (int i = 0; i < inputArray.length; i++) {
            if (inputArray[i] == 1) {
                CountOf1s = CountOf1s + 1;
            } else if (inputArray[i] == 2) {
                CountOf2s = CountOf2s + 1;
            }
        }
        int nearestVal = sumVal;
        if (sumVal % 2 == 1) {
            nearestVal = sumVal - 1;
        }

        int calculatedVal = 0;
        if (nearestVal / 2 >= CountOf2s) {
            calculatedVal = CountOf2s * 2;
            CountOf2s = 0;

        } else {
            calculatedVal = CountOf2s * 2;
            CountOf2s = CountOf2s - nearestVal / 2;

        }

        if (calculatedVal < nearestVal) {
            if (nearestVal - calculatedVal > CountOf1s) {
                //total sum of all values < nearestVal
                //Check if difference in nearestVal and total sum is 2 or 4 only if sumVal is even
                if (executedonce == 0) {
                    int remainingdiff = (nearestVal - calculatedVal) - CountOf1s;
                    //if (sumVal % 2 == 0) {
                    PossibleSolutionIfAllSplitFail = new int[inputArray.length + 2];
                    //PossibleSolutionIfAllSplitFail = inputArray;

                    for (int cn11 = 0; cn11 < inputArray.length; cn11++) {
                        PossibleSolutionIfAllSplitFail[cn11] = inputArray[cn11];
                    }

                    if ((remainingdiff) == 2) {
                        PossibleSolutionIfAllSplitFail[inputArray.length] = 1;
                        PossibleSolutionIfAllSplitFail[inputArray.length + 1] = 1;
                        executedonce = 1;
                    } else if ((remainingdiff) == 4) {
                        PossibleSolutionIfAllSplitFail[inputArray.length] = 2;
                        PossibleSolutionIfAllSplitFail[inputArray.length + 1] = 2;
                        executedonce = 1;
                    }
                }
                //}
                //return not possible
                return 0;
            } else if (nearestVal - calculatedVal == CountOf1s) {
                if (sumVal % 2 == 1) {
                    //No ones remaining to get the odd value
                    //Since sumVal is odd, it will never be obtained by adding 2 or 4 so dont check
                    //not poss
                    return 0;
                } else {
                    //possible ,and all +
                    return 1;
                }
            } else {
                CountOf1s = CountOf1s - (nearestVal - calculatedVal);
            }
        }

        int numOf2sToChangeSign = CountOf2s / 2;
        for (int j = 0; j < inputArray.length; j++) {
            if (inputArray[j] == 2 && numOf2sToChangeSign > 0) {
                inputArray[j] = -2;
                numOf2sToChangeSign -= 1;
            }

        }

        int numOf1sToChangeSign = CountOf1s / 2;
        for (int j = 0; j < inputArray.length; j++) {
            if (inputArray[j] == 1 && numOf1sToChangeSign > 0) {
                inputArray[j] = -1;
                numOf1sToChangeSign -= 1;
            }

        }

        int pairs1 = CountOf1s / 2;

        CountOf1s = CountOf1s % 2;
        CountOf2s = CountOf2s % 2;

        if (sumVal % 2 == 1) {
            if (CountOf1s == 0) {
                PossibleSolutionIfAllSplitFail = new int[inputArray.length + 2];
                for (int cnt = 0; cnt < inputArray.length; cnt++) {
                    //  PossibleSolutionIfAllSplitFail[cnt] =
                }

                //Since odd , will never be obtained by adding or subtracting 2 or 4
                return 0;
                //not possible
            } else {

                if (CountOf2s == 1) {
                    for (int j = 0; j < inputArray.length; j++) {
                        if (inputArray[j] == 1) {
                            inputArray[j] = -1;
                            break;
                        }

                    }
                }
                return 1;
            }
        } else {
            if (CountOf1s == 0 && CountOf2s == 0) {
                return 1;
                //possible
            } else {
                if (CountOf2s == 1 && pairs1 >= 1) {
                    for (int j = 0; j < inputArray.length; j++) {
                        if (inputArray[j] == 1) {
                            inputArray[j] = -1;
                            break;
                        }
                    }
                    return 1;
                }
                //check if total of remaining 1s and 2s can be nullified by adding or subtracting 2 1s or 2 2s
                if (executedonce == 0) {
                    int remainingdiff = CountOf1s + (CountOf2s * 2);
                    //if (sumVal % 2 == 0) {
                    PossibleSolutionIfAllSplitFail = new int[inputArray.length + 2];
                    // PossibleSolutionIfAllSplitFail = inputArray;
                    for (int cn11 = 0; cn11 < inputArray.length; cn11++) {
                        PossibleSolutionIfAllSplitFail[cn11] = inputArray[cn11];
                    }
                    if ((remainingdiff) == 2) {
                        PossibleSolutionIfAllSplitFail[inputArray.length] = -1;
                        PossibleSolutionIfAllSplitFail[inputArray.length + 1] = -1;
                        executedonce = 1;
                    } else if ((remainingdiff) == 4) {
                        PossibleSolutionIfAllSplitFail[inputArray.length] = -2;
                        PossibleSolutionIfAllSplitFail[inputArray.length + 1] = -2;
                        executedonce = 1;
                    }
                }
                //}

                //return not possible
                return 0;
            }
        }

        //return 0;
    }


    public static int getStepCount(int input1,int input2,int input3,int input4){
        int  diff_X, diff_Y;
        diff_X = input3 - input1;
        diff_Y = input4 - input2;

        if (diff_X==0 && diff_Y==0){
            return 0;
        }
        if (Math.abs(diff_X)==1 && Math.abs(diff_Y)==1){
            return 4;
        }

        int largercoordinate;
        int smallercoordinate;
        if (Math.abs(diff_X) == Math.abs(diff_Y)) {
            smallercoordinate = diff_X;
            largercoordinate = diff_Y;
        } else {
            largercoordinate = Math.abs(diff_Y) > Math.abs(diff_X) ? diff_Y : diff_X;
            smallercoordinate = largercoordinate == diff_Y ? diff_X : diff_Y;
        }
        // int largercoordinate =10;
        //int smallercoordinate = 4;

        int abs_larger = Math.abs(largercoordinate);

        int largercoord_factors_array[] = new int[(abs_larger / 2) + abs_larger % 2];
        inputArray = new int[largercoord_factors_array.length];
        for (int i = 0; i < largercoord_factors_array.length; i++) {
            if (i <= (abs_larger / 2) - 1) {
                largercoord_factors_array[i] = 2;
                inputArray[i] = 1;
            } else {
                largercoord_factors_array[i] = 1;
                inputArray[i] = 2;
            }
        }
        int outpt;
        if (diff_X == 0 && diff_Y == 0) {
            System.out.print("Same position");

        } else {
            while (true) {
                outpt = checkIfvalid_Count(inputArray, Math.abs(smallercoordinate));

                if (outpt == 1) {
                    if (smallercoordinate < 0) {
                        for (int i = 0; i < inputArray.length; i++) {
                            inputArray[i] = -(inputArray[i]);
                        }
                    }
                    if (largercoordinate < 0) {
                        for (int i = 0; i < largercoord_factors_array.length; i++) {
                            largercoord_factors_array[i] = -(largercoord_factors_array[i]);
                        }
                    }
                    break;
                } else {

                    int last2pos;

                    if (abs_larger / 2 == 0) {
                        last2pos = 1;
                    } else {
                        last2pos = largercoord_factors_array[(abs_larger / 2) - 1];
                    }

                    if (last2pos == 1) {
                        largercoord_factors_array = new int[PossibleSolutionIfAllSplitFail.length];
                        inputArray = new int[PossibleSolutionIfAllSplitFail.length];
                        for (int cnt2 = 0; cnt2 < largercoord_factors_array.length; cnt2++) {
                            inputArray[cnt2] = PossibleSolutionIfAllSplitFail[cnt2];
                            if (Math.abs(PossibleSolutionIfAllSplitFail[cnt2]) == 2) {
                                largercoord_factors_array[cnt2] = 1;
                            } else {
                                largercoord_factors_array[cnt2] = 2;
                            }

                        }
                        largercoord_factors_array[largercoord_factors_array.length - 1] = -(largercoord_factors_array[largercoord_factors_array.length - 1]);

                        outpt = checkIfvalid_Count(PossibleSolutionIfAllSplitFail, Math.abs(smallercoordinate));
                        //if (outpt == 1) {

                        if (smallercoordinate < 0) {
                            for (int i = 0; i < inputArray.length; i++) {
                                inputArray[i] = -(inputArray[i]);
                            }
                        }
                        if (largercoordinate < 0) {
                            for (int i = 0; i < largercoord_factors_array.length; i++) {
                                largercoord_factors_array[i] = -(largercoord_factors_array[i]);
                            }
                        }
                        break;
                        //}

                    } else {
                        int oldlargecoord_arrray[];
                        oldlargecoord_arrray = largercoord_factors_array;
                        largercoord_factors_array = new int[oldlargecoord_arrray.length + 1];
                        inputArray = new int[oldlargecoord_arrray.length + 1];
                        for (int i = 0, count2 = 0; i < largercoord_factors_array.length - 1; i++) {
                            if (count2 == 0 && oldlargecoord_arrray[i] == 2) {
                                largercoord_factors_array[i] = 1;
                                inputArray[i] = 2;
                                count2++;
                            } else {
                                largercoord_factors_array[i] = oldlargecoord_arrray[i];
                                if (largercoord_factors_array[i] == 2) {
                                    inputArray[i] = 1;
                                } else {
                                    inputArray[i] = 2;
                                }

                            }
                        }
                        largercoord_factors_array[largercoord_factors_array.length - 1] = 1;
                        inputArray[largercoord_factors_array.length - 1] = 2;
                    }
                }
                // System.out.print(outpt);
            }

            // System.out.print(outpt);

            /*if (Math.abs(diff_X) > Math.abs(diff_Y)) {
                System.out.println(Arrays.toString(largercoord_factors_array));
                System.out.println(Arrays.toString(inputArray));
            } else {
                System.out.println(Arrays.toString(inputArray));
                System.out.println(Arrays.toString(largercoord_factors_array));

            }*/

        }
        return largercoord_factors_array.length;
    }
}
