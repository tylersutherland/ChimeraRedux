For basic structure:
	In the main src folder there is a class DanForAllSeasons in that class the render method will call the render method
 of our different screen classes that we will create so keep that in mind when designing classes. For coding style, we never 
discussed it but we can come to a decision pretty easily just let me know what you guys prefer as I don't have a preference. 
I'd say stick to upper camel case for classes and lower camel case for methods. The main thing I want to settle is where we 
have braces otherwise let your concerns be known. When righting methods and classes make sure to comment on them in proper 
javadoc style, for example:

/**
 * Sets dans position on the screen
 *
 * @param x the coordinate on the horizontal axis
 * @param y the coordinate on the vertical axis, remember 0,0 is at the top left so increasing y makes dan go down
*/
public void setPosition(int x, int y){ ... }

Something that simple probably wouldn't need it but for any method that isn't just an accesor right them out.