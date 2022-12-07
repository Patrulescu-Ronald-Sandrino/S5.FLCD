public class ParserStrategy {
    public void expand(ParsingConfiguration configuration){

    }

    public void advance(ParsingConfiguration configuration){
        configuration.i += 1;
        configuration.alpha.add(configuration.beta.poll());
    }
    
    public void momentaryInsuccess(ParsingConfiguration configuration){
        configuration.s = ParsingState.BACK;
    }

    public void back(ParsingConfiguration configuration){
        configuration.i -= 1;
        configuration.beta.add(configuration.alpha.pop());
    }

    public void anotherTry(ParsingConfiguration configuration){
        
    }

    public void success(ParsingConfiguration configuration){
        configuration.s = ParsingState.FINAL;
    }
}
