
package cz.ctu.fit.ddw.skorpste.movie;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.config.TmdbConfiguration;

/**
 * @author Milan Dojchinovski
 * <milan (at) dojchinovski (dot) mk>
 * Twitter: @m1ci
 * www: http://dojchinovski.mk
 */
public class GATEEmbedded {

    public static void main(String[] args) {
        /*
        TmdbApi tmdbApi = new TmdbApi("d5a68adcd9c3b43761280d53514ae0ad");

        TmdbMovies movies = tmdbApi.getMovies();
        MovieDb movie = movies.getMovie(5353, "en");
        movie.getReviews();
        TmdbConfiguration configuration = tmdbApi.getConfiguration();
        */
        String review="The Wolf of Wall Street is so addicting to watch, that even with it's 3 hour long run time, you wont be surprised if you end up watching it four days in a row. This is Scorsese letting loose and having fun, showing all of the debauchery (and man, is there a lot) in all of it's glory. There are orgies, sex, nudity, copious amounts of drug use, and it now holds the record for the most use of fuck in any film. Leonardo DiCaprio gives a towering and hilarious performance as Jordan Belfort. He throws himself into the role with free abandon, while also showing that he's aces when it comes to comic timing and physical comedy. If he doesn't win an Oscar this season I'll be quite upset, though Matt from Dallas Buyers Club rightfully deserves it as well. But this is a 3 hour long film, and Leo is in every single second of every single scene. He's ferocious, hilarious, out of his mind, and flat out brilliant. The supporting players are tops as well. I was somewhat baffled when Jonah Hill earned a nominee for Moneyball, but this time around he rightfully deserves this years nominations. With his pearly white teeth, charisma, while also throwing himself into the role with no fear, this is without a doubt his greatest work as an actor to date. The real find, however, is Margot Robbie. Strikingly beautiful, her character could have been one note, but she also hits the ball out of the court. Even Matthew Macconaughey steals the show with only one scene. Dangerously funny, superbly edited, and with a delicious, irreverent, savage bite. The Wolf of Wall Street is Scorsese at his wildest. And it goes without saying, that this is one of the best films of last year. Just stay away if you are in any way a prude or conservative. Rated R- Graphic nudity, strong sexual content, drug use throughout, pervasive language, and some violence.";
        GateClient client = new GateClient();        
        client.run(review);    
    }
}
