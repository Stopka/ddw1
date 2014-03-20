package cz.ctu.fit.ddw.skorpste.movie;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.Reviews;
import info.movito.themoviedbapi.model.config.TmdbConfiguration;
import info.movito.themoviedbapi.model.core.MovieResults;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Milan Dojchinovski
 * <milan (at) dojchinovski (dot) mk>
 * Twitter: @m1ci www: http://dojchinovski.mk
 */
public class GATEEmbedded {

    public static void main(String[] args) {
        int film_id=Integer.parseInt(args[0]);
        TmdbApi tmdbApi = new TmdbApi("d5a68adcd9c3b43761280d53514ae0ad");
        
        TmdbMovies movies = tmdbApi.getMovies();
        //MovieResults movier = movies.getTopRatedMovies("en", 1);//
        //MovieDb movie=movier.getResults().get(1);
        MovieDb movie=movies.getMovie(film_id, "en");
        if(movie==null){
            System.err.println("No film");
            return;
        }
        System.out.println(movie.getTitle());
        System.out.println("id: "+movie.getId());
        
        /*List<Reviews> lr = movie.getReviews();
        if(lr==null){
            System.err.println("No reviews");
            return;
        }*/
        //TmdbConfiguration configuration = tmdbApi.getConfiguration();
        
        List<Reviews> lr=tmdbApi.getReviews().getReviews(film_id, "en",0);
        if(lr==null){
            System.err.println("No reviews");
            return;
        }
        /**/
        GateClient client = new GateClient();
        int pos = 0;
        int neg = 0;
        System.out.println("============================================");
        for (Reviews r : lr) {
            String content = r.getContent();

            System.out.println("author: "+r.getAuthor());
            System.out.println("url: "+r.getUrl());
            System.out.println(r.getContent());
            System.out.println("--------------------------------------------");
            int mark = client.run(content);
            if (mark < 0) {
                pos++;
            }
            if (mark > 0) {
                neg++;
            }
            System.out.println("============================================");
        }
        System.out.println(movie.getTitle());
        int sum=lr.size();
        System.out.println("sum:" + sum + " 100%");
        double poss = (double) pos / (double) (sum) * 100;
        System.out.println("pos:" + pos + " " + poss + "%");
        double negs = (double) neg / (double) (sum) * 100;
        System.out.println("neg:" + neg + " " + negs + "%");
        double limit = 60;
        if (poss > limit) {
            System.out.println("Positive! (+)");
        } else if (negs > limit) {
            System.out.println("Negative! (-)");
        } else {
            System.out.println("Neutral! (*)");
        }
    }
}
