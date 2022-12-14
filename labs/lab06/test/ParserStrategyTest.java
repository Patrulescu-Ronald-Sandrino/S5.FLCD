import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

public class ParserStrategyTest {
    //region fields
    private static final Map<Set<String>, Set<List<String>>> PRODUCTION_RULES = getProductionRules();
    ParserStrategy parserStrategy = new ParserStrategy(PRODUCTION_RULES);
    ParsingConfiguration initialConfig = new ParsingConfiguration(Arrays.asList("S"));
    //endregion


    @Test
    public void expand() {
        ParsingConfiguration config = initialConfig;
        parserStrategy.expand(config);

        assert config.next != null;
        assert config.next.s == ParsingState.NORMAL;
        assert config.next.i == 1;
        assert config.next.alpha.equals(Arrays.asList("S"));
        assert config.next.indexMapping.equals(new LinkedHashMap<>() {{
            put(config.alpha.size(), 1);
        }});
        assert config.next.beta.equals(Util.reverse(Arrays.asList("a", "S", "b", "S")));
    }

    @Test
    public void advance() {
        ParsingConfiguration config = initialConfig;
        parserStrategy.expand(config);
        config = config.next;
        parserStrategy.advance(config);

        assert config.next != null;
        assert config.next.s == ParsingState.NORMAL;
        assert config.next.i == 2;
        assert config.next.alpha.equals(Arrays.asList("S", "a"));
        assert config.next.indexMapping.equals(new LinkedHashMap<>() {{
            put(0, 1);
        }});
        assert config.next.beta.equals(Util.reverse(Arrays.asList("S", "b", "S")));
    }

    @Test
    public void momentaryInsuccess() {
        ParsingConfiguration config = new ParsingConfiguration(
                ParsingState.NORMAL,
                3,
                Arrays.asList("S", "a", "S", "a", "S"),
                new LinkedHashMap<>() {{
                    put(0, 1);
                    put(2, 1);
                    put(4, 1);
                }},
                Arrays.asList("a", "S", "b", "S", "b", "S", "b", "S"));
        parserStrategy.momentaryInsuccess(config);

        assert config.next.s == ParsingState.BACK;
        assert config.next.i == config.i;
        assert config.next.alpha.equals(config.alpha);
        assert config.next.indexMapping.equals(config.indexMapping);
        assert config.next.beta.equals(config.beta);
    }

    @Test
    public void back() {
        // i--
        // alpha.pop()
        // beta.push(alpha.peek())
        ParsingConfiguration config = new ParsingConfiguration(
                ParsingState.BACK,
                6,
                Arrays.asList("S", "a", "S", "a", "S", "c", "b", "S", "c"),
                new LinkedHashMap<>() {{
                    put(0, 1);
                    put(2, 1);
                    put(4, 3);
                    put(7, 3);
                }},
                Arrays.asList("b", "S"));
        parserStrategy.back(config);

        assert config.next.s == ParsingState.BACK;
        assert config.next.i == config.i - 1;
        assert config.next.alpha.equals(Arrays.asList("S", "a", "S", "a", "S", "c", "b", "S"));
        assert config.next.indexMapping.equals(new LinkedHashMap<>() {{
            put(0, 1);
            put(2, 1);
            put(4, 3);
            put(7, 3);
        }});
        assert config.next.beta.equals(Util.reverse(Arrays.asList("c", "b", "S")));
    }

    @Test
    public void anotherTry() {
        anotherTryI();
        anotherTryII();
        anotherTryIII();
    }

    @Ignore
    public void anotherTryI() {
                ParsingConfiguration config = new ParsingConfiguration(
                ParsingState.BACK,
                2,
                Arrays.asList("S", "a", "S"),
                new LinkedHashMap<>() {{
                    put(0, 1);
                    put(2, 1);
                }},
                Arrays.asList("a", "S", "b", "S", "b", "S"));

                parserStrategy.anotherTry(config);

                assert config.next.s == ParsingState.NORMAL;
                assert config.next.i == config.i;
                assert config.next.alpha.equals(Arrays.asList("S", "a", "S"));
                assert config.next.indexMapping.equals(new LinkedHashMap<>() {{
                    put(0, 1);
                    put(2, 2);
                }});
                assert config.next.beta.equals(Util.reverse(Arrays.asList("a", "S", "b", "S")));
    }

    @Ignore
    public void anotherTryII() {
                ParsingConfiguration config = new ParsingConfiguration(
                ParsingState.BACK,
                3,
                Arrays.asList("S", "a", "S", "a", "S"),
                new LinkedHashMap<>() {{
                    put(0, 1);
                    put(2, 1);
                    put(4, 3);
                }},
                Arrays.asList("c", "b", "S", "b", "S"));

                parserStrategy.anotherTry(config);

                assert config.next.s == config.s;
                assert config.next.i == config.i;
                assert config.next.alpha.equals(Arrays.asList("S", "a", "S", "a"));
                assert config.next.indexMapping.equals(new LinkedHashMap<>() {{
                    put(0, 1);
                    put(2, 1);
                }});
                assert config.next.beta.equals(Util.reverse(Arrays.asList("S", "b", "S", "b", "S")));
    }

    @Ignore
    public void anotherTryIII() {
        ParsingConfiguration config = new ParsingConfiguration(
                ParsingState.BACK,
                1,
                List.of("S"),
                new LinkedHashMap<>() {{
                    put(0, 3);
                }},
                Arrays.asList("c")
        );

        parserStrategy.anotherTry(config);

        assert config.next.s == ParsingState.ERROR;
        assert config.next.i == config.i;
        assert config.next.alpha.equals(List.of());
        assert config.next.beta.equals(Util.reverse(List.of()));

    }

    @Test
    public void success() {
        ParsingConfiguration config = new ParsingConfiguration(
                ParsingState.NORMAL,
                6,
                Arrays.asList("S", "a", "S", "a", "S", "c", "b", "S", "c"),
                new LinkedHashMap<>() {{
                    put(0, 1);
                    put(2, 2);
                    put(4, 3);
                    put(7, 3);
                }},
                List.of());
        parserStrategy.success(config);

        assert config.next.s == ParsingState.FINAL;
        assert config.next.i == config.i;
        assert config.next.alpha.equals(config.alpha);
        assert config.next.indexMapping.equals(config.indexMapping);
        assert config.next.beta.equals(config.beta);
    }

    //region utility methods
    private static Map<Set<String>, Set<List<String>>> getProductionRules() {
        Set<List<String>> rhsOfProductionRule = new LinkedHashSet<>();
        rhsOfProductionRule.add(Arrays.asList("a", "S", "b", "S"));
        rhsOfProductionRule.add(Arrays.asList("a", "S"));
        rhsOfProductionRule.add(Arrays.asList("c"));

        Map<Set<String>, Set<List<String>>> productionRules = new LinkedHashMap<>();
        productionRules.put(new LinkedHashSet<>(Collections.singleton("S")), rhsOfProductionRule);
        return productionRules;
    }
    //endregion
}