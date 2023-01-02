package com.cookandroid.myapplicationx;

public class Question {

    public String questions[] = {
            "포인터 자료형에 대한 설명으로 옳지 않은 것은?",
            "다음 중 절차적 언어에 대한 설명으로 틀린 것은?",
            "c언에에서 정수가 2Byte로 표현되고, int a[2][3] 으로 선언된 배열의 첫 번째 자료가 1000번지에 저장 되었다. 이때 a[1][1]원소가 저장된 주소는?",
            "분산 컴퓨팅 환경에서 서로 다른 기종 간의 하드웨어나 프로토콜, 통신환경 등을 연결하여 응용프로그램과 운영환경 간에 원만한 통신이 이루어질 수 있게 서비스를 제공하는 소프트웨어는?",
            " GoF(Gangs of Four) 디자인 패턴의 생성패턴에 속하지 않는 것은?"
    };

    public String choices[][] = {
            {"고급 언어에서 사용되지 않고 저급 언어에서 사용되는 기법이다.", "객체를 참조하기 위해 주소를 값으로 하는 형식이다.", "커다란 배열에 원소를 효율적으로 저장하고자 할 때 이용한다.", "하나의 자료에 동시에 많은 리스트의 연결이 가능하다."},
            {"컴퓨터의 처리 구조와 유사하여 실행속도가 빠르다.", "실행되는 절차를 중시한다.", "데이터 중심으로 프로시저를 구현한다.", "상속을 통한 재사용성이 높다."},
            {"1002", "1004", "1006", "1008"},
            {"미들웨어","하드웨어","오픈 허브웨어","그레이웨어"},
            {"추상 팩토리, 빌더, 어댑터, 싱글턴"}
    };

    public String correctAnswer[] = {
            "고급 언어에서 사용되지 않고 저급 언어에서 사용되는 기법이다.",
            "상속을 통한 재사용성이 높다.",
            "1008",
            "미들웨어",
            "어댑터"
    };

    public String getQuestion(int a){
        String question = questions[a];
        return question;
    }

    public String getchoice1(int a){
        String choice = choices[a][0];
        return choice;
    }

    public String getchoice2(int a){
        String choice = choices[a][1];
        return choice;
    }

    public String getchoice3(int a){
        String choice = choices[a][2];
        return choice;
    }

    public String getchoice4(int a){
        String choice = choices[a][3];
        return choice;
    }

    public String getCorrectAnswer(int a){
        String answer = correctAnswer[a];
        return answer;
    }

}
