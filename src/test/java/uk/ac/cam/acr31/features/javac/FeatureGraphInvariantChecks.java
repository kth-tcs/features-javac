package uk.ac.cam.acr31.features.javac;

import static com.google.common.truth.Truth.assertThat;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import uk.ac.cam.acr31.features.javac.graph.FeatureGraph;
import uk.ac.cam.acr31.features.javac.proto.GraphProtos.FeatureEdge.EdgeType;
import uk.ac.cam.acr31.features.javac.proto.GraphProtos.FeatureNode.NodeType;
import uk.ac.cam.acr31.features.javac.testing.TestCompilation;

@RunWith(JUnit4.class)
public class FeatureGraphInvariantChecks {

  private FeatureGraph featureGraph;

  @Before
  public void setup() {
    TestCompilation compilation =
        TestCompilation.compile(
            "Test.java", //
            "public class Test {",
            "  public static void main(String[] args) {",
            "    int a = 5;",
            "  }",
            "}");
    featureGraph =
        FeaturePlugin.createFeatureGraph(compilation.compilationUnit(), compilation.context());
  }

  @Test
  public void featureGraph_containsSingleAstRoot() {
    assertThat(featureGraph.nodes(NodeType.AST_ROOT)).hasSize(1);
  }

  @Test
  public void featureGraph_tokensHaveSingleRelatedAstNode() {
    assertThat(
            featureGraph
                .nodes(NodeType.TOKEN)
                .stream()
                .map(
                    node ->
                        featureGraph.predecessors(node, EdgeType.ASSOCIATED_TOKEN).stream().count())
                .anyMatch(count -> count != 1))
        .isFalse();
  }

  @Test
  public void featureGraph_nextTokenFormsASequence() {
    Map<Long, Long> successorCounts =
        featureGraph
            .nodes(NodeType.TOKEN)
            .stream()
            .map(node -> featureGraph.successors(node, EdgeType.NEXT_TOKEN).stream().count())
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    Map<Long, Long> predecessorCounts =
        featureGraph
            .nodes(NodeType.TOKEN)
            .stream()
            .map(node -> featureGraph.predecessors(node, EdgeType.NEXT_TOKEN).stream().count())
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    assertThat(successorCounts.keySet()).containsExactly(0L, 1L);
    assertThat(successorCounts.get(0L)).isEqualTo(1);
    assertThat(predecessorCounts.keySet()).containsExactly(0L, 1L);
    assertThat(predecessorCounts.get(0L)).isEqualTo(1);
  }
}
