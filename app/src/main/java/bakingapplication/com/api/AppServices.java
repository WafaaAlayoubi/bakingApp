package bakingapplication.com.api;

import java.util.List;

import bakingapplication.com.models.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

public interface AppServices {

    @GET("baking.json")
    Call<List<Recipe>> getRecipes();

}
