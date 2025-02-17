import java.util.Scanner;

public class Omok {
    static Scanner s = new Scanner(System.in);
    public static final String ANSI_RESET = "\033[0m"; // Text Reset
    public static final String ANSI_GOLD = "\033[0;33m";   // GOLD
    public static final String ANSI_BLACK_BOLD = "\033[1;30m"; // BLACK
    public static final String ANSI_CYAN_BOLD = "\033[1;36m"; // CYAN
    public static final String ANSI_WHITE_BOLD = "\033[1;37m"; // WHITE
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m"; // BLACK
    public static final String ANSI_RED_BOLD_BRIGHT = "\033[1;91m"; // RED
    public static final String ANSI_WHITE_BACKGROUND_BRIGHT = "\033[0;107m"; // WHITE
    public static final String ANSI_GREEN_BOLD = "\033[1;32m";// GREEN
    public static final String ANSI_GOLD_BOLD = "\033[1;33m";   // GOLD
    // 프로젝트 > Properties > Resource > Text file encoding- other: UTF-8 변경 필요
    static int[][] goBoard = new int[19][19]; // 흑: 1, 백: 2
    static int startTime;            // 시작 시간
    static int elapsedTime;            // 경과 시간
    // 배열의 인덱스 0 = 흑, 1 = 백
    static String[] name = new String[2];
    static int[] Timeout = {3, 3},
            Time = {300, 300}, // 제한시간 5분
            win = {0, 0};
    static boolean[] Timer = {false, false}; // 5분 지나면 30초 타이머 on(=true)

    // 게임시작(bw: 흑 0, 백1)
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        System.out.println("---------");
        System.out.println(": " + ANSI_GOLD_BOLD + "오목게임 " + ANSI_RESET + " :");
        System.out.println("--------- ver 1.0");
        System.out.println("흑돌 사용자 명을 입력해주세요.");
        name[0] = s.nextLine();
        System.out.println("백돌 사용자 명을 입력해주세요.");
        name[1] = s.nextLine();
        System.out.println(nameColor(0) + ", " + nameColor(1) + "님 환영합니다.");
        System.out.println("오목 게임은 3판 2선승제로 2번 먼저 이기시면 게임에서 최종 승리합니다.");
        System.out.println("시작하시려면 " + ANSI_CYAN_BOLD + "Enter" + ANSI_RESET + "를 눌러주세요.");
        s.nextLine();
        for (int i = 1; win[0] < 2 && win[1] < 2; i++) {
            if (i > 1) {
                resetSetting();
            }
            System.out.println(i + "번째 게임을 시작합니다.");
            printBoard();
            while (true) {
                if (gameStart(0)) {
                    break;
                }
                if (gameStart(1)) {
                    break;
                }
            }
        }
        if (win[0] == 2) {
            System.out.println(ANSI_GOLD_BOLD + "-게임종료- \n최종 승자는 " + nameColor(0) + ANSI_GOLD_BOLD + "님입니다.");
        } else {
            System.out.println(ANSI_GOLD_BOLD + "-게임종료- \n최종 승자는 " + nameColor(1) + ANSI_GOLD_BOLD + "님입니다.");
        }
    }

    // 오목판 출력
    public static void printBoard() {
        System.out.println(
                ANSI_GREEN_BOLD + "     0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18" + ANSI_RESET);    // 흰색 바탕에서는 안보여서 색상 바꿈.
        System.out.println("     ╷  ╷  ╷  ╷  ╷  ╷  ╷  ╷  ╷  ╷  ╷  ╷  ╷  ╷  ╷  ╷  ╷  ╷  ╷");
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (goBoard[i][j] == 0) {
                    if (i == 0) {
                        if (j == 0) {
                            System.out.print(ANSI_GREEN_BOLD + i + ANSI_RESET + "  ─ ┌─"); // ─, │, ┼, ┌, ┐, └, ┘
                            // 는 특수기호
                        } else if (j == 18) {
                            System.out.println("─┐");
                        } else {
                            System.out.print("─┬─");
                        }
                    } else if (i == 18) {
                        if (j == 0) {
                            System.out.print(ANSI_GREEN_BOLD + i + ANSI_RESET + " ─ └─");
                        } else if (j == 18) {
                            System.out.println("─┘");
                        } else {
                            System.out.print("─┴─"); // ⏊ ⎡ ⎣ ⎤ ⎦ ⎾ ⎿⏋⏌
                        }
                    } else {
                        if (j == 0) {
                            if (i <= 9) {
                                System.out.print(ANSI_GREEN_BOLD + i + ANSI_RESET + "  ─ ├─");
                            } else {
                                System.out.print(ANSI_GREEN_BOLD + i + ANSI_RESET + " ─ ├─");
                            }
                        } else if (j == 18) {
                            System.out.println("─┤");
                        } else {
                            System.out.print("─┼─");
                        }
                    }
                } else if (goBoard[i][j] == 1) {
                    if (j == 0) {
                        if (i <= 9) {
                            System.out.print(ANSI_GREEN_BOLD + i + ANSI_RESET + "  - ● ");
                        } else {
                            System.out.print(ANSI_GREEN_BOLD + i + ANSI_RESET + " - ● ");
                        }
                    } else if (j == 18) {
                        System.out.println(" ●");
                    } else {
                        System.out.print(" ● ");
                    }
                } else if (goBoard[i][j] == 2) {
                    if (j == 0) {
                        if (i <= 9) {
                            System.out.print(ANSI_GREEN_BOLD + i + ANSI_RESET + "  - ○ ");
                        } else {
                            System.out.print(ANSI_GREEN_BOLD + i + ANSI_RESET + " - ○ ");
                        }


                    } else if (j == 18) {
                        System.out.println(" ○");
                    } else {
                        System.out.print(" ○ ");
                    }
                }
            }
        }
    }

    // 세팅 초기화 및 세트 스코어 출력
    public static void resetSetting() {
        System.out.println("현재 스코어= " + win[0] + " : " + win[1] + "\n================" + ANSI_RESET);
        System.out.println("다음 게임으로 넘어갑니다.");
        System.out.println("계속하려면 엔터를 눌러주세요."); // 다음 게임 진행 전 잠시 대기
        s.nextLine();
        for (int i = 0; i < 2; i++) {
            Timeout[i] = 3;
            Timer[i] = false;
            Time[i] = 300;
        }
        resetBoard(goBoard);
    }

    // 오목판 초기화
    public static void resetBoard(int[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                a[i][j] = 0;
            }
        }
    }

    // 착수
    public static boolean gameStart(int num) {
        int num2;
        if (num == 0) {
            num2 = 1;
        } else {
            num2 = 0;
        }
        System.out.println(nameColor(num) + "님의 차례입니다. 좌표를 x,y 형태로 입력해주세요."
                + "(000을 누르면 기권 패배로 처리합니다.)");    // 기권 번호 안내
        System.out.println("남은 시간 : " + ANSI_BLACK_BACKGROUND + ANSI_WHITE_BOLD + "흑" + ANSI_RESET + " "
                + Time[0] / 60 + "분 " + Time[0] % 60 + "초  /  "
                + ANSI_WHITE_BACKGROUND_BRIGHT + ANSI_BLACK_BOLD + "백" + ANSI_RESET + " "
                + Time[1] / 60 + "분 " + Time[1] % 60 + "초 ");    // 남은 시간
        int result;
        if (Timer[num]) { // 5분 제한시간 끝났을 때
            System.out.println(ANSI_RED_BOLD_BRIGHT + "제한시간이 지났습니다. 이제부터 30초 타이머가 작동됩니다.\n타이머에 3번 걸리면 자동으로 패배합니다." +
                    "\n남은 횟수는 " + Timeout[num] + "번 입니다." + ANSI_RESET);
            stopwatch(0);
            result = coordinateIn(num + 1);
            if (stopwatch(num + 1)) {
                Timeout[num]--;
                System.out.println(ANSI_RED_BOLD_BRIGHT + "30초 타이머에 걸렸습니다." +
                        "\n남은 횟수는 " + Timeout[num] + "번 입니다.\n 2초 후에 다음 플레이어의 턴으로 넘어갑니다." + ANSI_RESET);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Time[num] = 30; // 30초 타이머 초기화
        } else {
            stopwatch(0);
            result = coordinateIn(num + 1);
            Timer[num] = stopwatch(num + 1);
            if (Timer[num]) { // 5분 제한시간 끝났을 때
                Time[num] = 30; // 타이머 30초로 설정
            }
        }
        if (Timeout[num] == 0) { // 타임아웃
            win[num2]++;
            System.out.println(nameColor(num2) + ANSI_GOLD + "님이 이겼습니다.");
            return true;
        }
        printBoard();
        if (result == -1) {
            win[num]++;
            System.out.println(nameColor(num) + ANSI_GOLD + "님이 이겼습니다.");
            return true;
        } else if (result == -2) {
            win[num2]++;
            System.out.println(nameColor(num2) + ANSI_GOLD + "님이 이겼습니다.");
            return true;
        }
        return false;
    }

    // 좌표입력
    public static int coordinateIn(int bw) {
        while (true) {
            int[] coor = new int[2];
            String in = s.nextLine();
            if (in.isEmpty()) { // 좌표 입력 안 했을때
                System.out.println(ANSI_RED_BOLD_BRIGHT + "좌표를 입력하지 않았습니다. 다시 입력해주세요" + ANSI_RESET);
                continue;
            } else if (in.equals("000")) { // 기권
                return -2;
            } else {
                int k = 0;
                for (int i = 0; i < in.length(); i++) { // 사용자에게 받은 문자열을 좌표로 변환
                    if (in.charAt(i) >= 48 && in.charAt(i) <= 57) {
                        coor[k] *= 10;
                        coor[k] += in.charAt(i) - 48;
                    } else {
                        k++;
                    }
                }
                if (coor[0] > 18 || coor[1] > 18) { // 오목판을 벗어나는 숫자 금지
                    System.out.println(ANSI_RED_BOLD_BRIGHT + "오목판 범위를 벗어납니다. 다시 입력해주세요" + ANSI_RESET);
                    continue;
                }
                if (goBoard[coor[0]][coor[1]] == 1 || goBoard[coor[0]][coor[1]] == 2) { // 중복 방지
                    System.out.println(ANSI_RED_BOLD_BRIGHT + "중복입니다. 다시 입력해주세요" + ANSI_RESET);
                    continue;
                } else {
                    goBoard[coor[0]][coor[1]] = bw;
                    if (prohibitDoubleThree(coor[0], coor[1], bw)) { // 삼삼 금지
                        goBoard[coor[0]][coor[1]] = 0;
                        System.out.println(ANSI_RED_BOLD_BRIGHT + "삼삼규칙에 어긋납니다. 다시 입력해주세요" + ANSI_RESET);
                        continue;
                    }
                    if (victoryFive(coor[0], coor[1], bw)) {
                        return -1; // 오목 달성
                    }
                }
            }
            break;
        }
        return 0;
    }

    // 흑, 백 사용자에 컬러 입히기
    public static String nameColor(int num) {
        if (num == 0) {
            return ANSI_BLACK_BACKGROUND + ANSI_WHITE_BOLD + name[0] + ANSI_RESET;
        } else if (num == 1) {
            return ANSI_WHITE_BACKGROUND_BRIGHT + ANSI_BLACK_BOLD + name[1] + ANSI_RESET;
        }
        return null;
    }

    // 오목달성
    public static boolean victoryFive(int i, int j, int bw) {
        int[][] vic = stoneConsecutive(i, j, 5, bw);
        for (int h = 0; h < 4; h++) {
            if (vic[h][0] == 5) {
                return true;
            }
        }
        return false;
    }

    // 연속하는 돌의 개수
    public static int[][] stoneConsecutive(int i, int j, int num, int bw) {
        int stoneHorizontal = 0; // 가로로 연속으로 같은 색의 돌일 경우 집계하는 변수
        int stoneVertical = 0; // 세로로 연속으로 같은 색의 돌일 경우 집계하는 변수
        int stoneDiagonalRight = 0; // 오른쪽 대각선(/)으로 연속으로 같은 색의 돌일 경우 집계하는 변수
        int stoneDiagonalLeft = 0; // 왼쪽 대각선()으로 연속으로 같은 색의 돌일 경우 집계하는 변수
        int[][] conse = new int[4][3];
        for (int h = j - num + 1; h < j + num; h++) {
            if (h < 0 || h > 18) {
                continue;
            }
            if (goBoard[i][h] == bw) {
                stoneHorizontal++;
            } else {
                stoneHorizontal = 0;
            }
            if (stoneHorizontal == num) {
                conse[0][0] = stoneHorizontal;
                conse[0][1] = i; // 시작점
                conse[0][2] = h - num + 1;
                break;
            }
        }
        for (int h = i - num + 1; h < i + num; h++) {
            if (h < 0 || h > 18) {
                continue;
            }
            if (goBoard[h][j] == bw) {
                stoneVertical++;
            } else {
                stoneVertical = 0;
            }
            if (stoneVertical == num) {
                conse[1][0] = stoneVertical;
                conse[1][1] = h - num + 1; // 시작점
                conse[1][2] = j;
                break;
            }
        }
        int s = i - num + 1; // i좌표 변환
        for (int h = j - num + 1; h < j + num; h++) {
            if (h < 0 || h > 18 || s < 0 || s > 18) {
                s++;
                continue;
            }
            if (goBoard[s][h] == bw) {
                s++;
                stoneDiagonalRight++;
            } else {
                s++;
                stoneDiagonalRight = 0;
            }
            if (stoneDiagonalRight == num) {
                conse[2][0] = stoneDiagonalRight;
                conse[2][1] = s - num; // 시작점
                conse[2][2] = h - num + 1;
                break;
            }
        }
        s = i + num - 1;
        for (int h = j - num + 1; h < j + num; h++) {
            if (h < 0 || h > 18 || s < 0 || s > 18) {
                s--;
                continue;
            }
            if (goBoard[s][h] == bw) {
                s--;
                stoneDiagonalLeft++;
            } else {
                s--;
                stoneDiagonalLeft = 0;
            }
            if (stoneDiagonalLeft == num) {
                conse[3][0] = stoneDiagonalLeft;
                conse[3][1] = s + num; // 시작점
                conse[3][2] = h - num + 1;
                break;
            }
        }
        return conse;
    }

    // 3x3 금지 기능
    public static boolean prohibitDoubleThree(int i, int j, int bw) { // call 받는 변수 필요
        int[][] DoubleThree = stoneConsecutive(i, j, 3, bw); // 가로, 세로, /, \ 로 연속으로 놓은 돌의 개수
        int[][] conseAgain = new int[4][3]; //stoneConsecutive 다시 돌리고 저장할 배열
        if (DoubleThree[0][0] == 3) { // 기준점 가로
            int y = DoubleThree[0][1];
            int x = DoubleThree[0][2];
            for (int s = 1; s < 4; s++) {
                conseAgain = stoneConsecutive(y, x, 3, bw);
                for (int h = 0; h < 4; h++) {
                    if (conseAgain[h][0] == 3 && h != 0) {
                        return true;
                    }
                }
                x++;
            }
        }
        if (DoubleThree[1][0] == 3) { // 기준점 세로
            int y = DoubleThree[1][1];
            int x = DoubleThree[1][2];
            for (int s = 1; s < 4; s++) {
                conseAgain = stoneConsecutive(y, x, 3, bw);
                for (int h = 0; h < 4; h++) {
                    if (conseAgain[h][0] == 3 && h != 1) {
                        return true;
                    }
                }
                y++;
            }
        }
        if (DoubleThree[2][0] == 3) { // 기준점 오른쪽 대각선
            int y = DoubleThree[2][1];
            int x = DoubleThree[2][2];
            for (int s = 1; s < 4; s++) {
                conseAgain = stoneConsecutive(y, x, 3, bw);
                for (int h = 0; h < 4; h++) {
                    if (conseAgain[h][0] == 3 && h != 2) {
                        return true;
                    }
                }
                y++;
                x++;
            }
        }
        if (DoubleThree[3][0] == 3) { // 기준점 왼쪽 대각선
            int y = DoubleThree[3][1];
            int x = DoubleThree[3][2];
            for (int s = 1; s < 4; s++) {
                conseAgain = stoneConsecutive(y, x, 3, bw);
                for (int h = 0; h < 4; h++) {
                    if (conseAgain[h][0] == 3 && h != 3) {
                        return true;
                    }
                }
                y--;
                x++;
            }
        }
        return false;
    }

    // 5분 스톱워치
    public static boolean stopwatch(int onOff) {
        if (onOff == 0) {        // 타이머 켜기
            startTime = (int) System.currentTimeMillis() / 1000;
        }
        if (onOff == 1 || onOff == 2) {        // 타이머 끄기
            int endTime = (int) System.currentTimeMillis() / 1000;
            elapsedTime = endTime - startTime;
            int num = onOff - 1;
            if (Timer[num]) {
                Time[num] = 30 - elapsedTime;
            } else {
                Time[num] -= elapsedTime;
            }
            if (Time[num] <= 0) {
                Time[num] = 0;
                return true;
            }
        }
        return false;
    }

}

