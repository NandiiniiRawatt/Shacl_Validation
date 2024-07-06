from rdflib import Graph
from pyshacl import validate
import time
import numpy as np

def load_graph(file_path):
    g = Graph()
    g.parse(file_path, format='turtle')
    return g

def save_report(report_text, run_number):
    with open(f"validation_report_run_{run_number}.txt", "w") as report_file:
        report_file.write(report_text)

def run_validation(data_graph_path, shapes_graph_path, runs=10):
    validation_times = []

    for i in range(runs):
        data_graph = load_graph(data_graph_path)
        shapes_graph = load_graph(shapes_graph_path)

        start_time = time.time()
        conforms, results_graph, report_text = validate(
            data_graph,
            shacl_graph=shapes_graph,
            abort_on_first=False,
            meta_shacl=False,
            advanced=True,
            debug=False
        )
        end_time = time.time()

        validation_time = end_time - start_time
        validation_times.append(validation_time)
        
        print(f"Run {i+1}: Validation Time: {validation_time:.2f} seconds")
        print(f"Validation Result: {'Conforms' if conforms else 'Does not conform'}")
        save_report(report_text, i+1)

    # Calculate overall statistics
    average_time = np.mean(validation_times)
    overall_std_deviation = np.std(validation_times)
    print(f"\nAverage Validation Time: {average_time:.2f} seconds")
    print(f"Avg Standard Deviation: {overall_std_deviation:.2f} seconds")

if __name__ == "__main__":
    data_graph_path = r"C:\Users\nandi\OneDrive\Pictures\Screenshots\documents\TUM\4th Sem\BPC\Shacl Validation\airroutes.ttl"
    shapes_graph_path = r"C:\Users\nandi\OneDrive\Pictures\Screenshots\documents\TUM\4th Sem\BPC\Shacl Validation\shacl-shapes1.ttl"
    run_validation(data_graph_path, shapes_graph_path, runs=10)
