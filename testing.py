from rdflib import Graph
from pyshacl import validate

# Load the RDF data
rdf_file_path = r'C:\Users\nandi\OneDrive\Pictures\Screenshots\documents\TUM\4th Sem\BPC\Shacl Validation\airroutes.ttl'
rdf_graph = Graph()
rdf_graph.parse(rdf_file_path, format='turtle')

# Load the SHACL constraints
shacl_file_path = r'C:\Users\nandi\OneDrive\Pictures\Screenshots\documents\TUM\4th Sem\BPC\Shacl Validation\shacl-shapes1.ttl'
shacl_graph = Graph()
shacl_graph.parse(shacl_file_path, format='turtle')

# Validate the RDF data against the SHACL constraints
conforms, results_graph, results_text = validate(
    rdf_graph,
    shacl_graph=shacl_graph,
    ont_graph=None,
    abort_on_first=False,
    meta_shacl=False,
    advanced=True,
    js=False
)

print(results_text)
