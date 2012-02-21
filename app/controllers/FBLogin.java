package controllers;

import controllers.securesocial.SecureSocial;
import play.mvc.Controller;
import play.mvc.With;
import securesocial.provider.SocialUser;

@With( SecureSocial.class )
public class FBLogin extends Controller {

	public static void index() {
		SocialUser user = SecureSocial.getCurrentUser();
		render(user);
	}
}
