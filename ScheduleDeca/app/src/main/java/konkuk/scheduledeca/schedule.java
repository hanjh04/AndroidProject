package konkuk.scheduledeca;

/**
 * Created by hjh on 2016-06-20.
*/
public class schedule {
    String task;
    int start;
    int time;
    int a_time;
    int R, G, B;
    //color c;

    public schedule(String task, int start, int time, int a_time) {
        this.task = task;
        this.start = start;
        this.time = time;
        this.a_time = a_time;
//            part.setTint(color(255,lifespan));

        this.R = (int)Math.random()*155+100;
        this.G = (int)Math.random()*155+100;
        this.B = (int)Math.random()*155+100;
        //c = color(R, G, B);
    }

    public schedule[] addSchedule(schedule[] today_works, schedule s) {
        schedule[] n_works = new schedule[today_works.length + 1];
        n_works = today_works;
//        arrayCopy(today_works, n_works);
        n_works[n_works.length - 1] = s;
        return n_works;
    }
}