import sys
import random

def gen():
    #Numero de pontos
    num_pontos = int(sys.argv[1])
    i = 0
    coord = []

    #Gera pontos aleatorios e guarda 10 em cada linha
    with open("input.txt", "w") as f:
        f.write(str(num_pontos) + "\n")
        while i<num_pontos:
            j = 0
            lista = []
            while (j<10) and (i<num_pontos):
<<<<<<< HEAD
                i=i+1
                j=j+1
                x = random.randint(0,9999999)
                y = random.randint(0,9999999)
=======
                i+=1
                j+=1
                x = random.randint(0,99999)
                y = random.randint(0,99999)
>>>>>>> e5a6b9723da6813e68aa864d6eaed68bf03da113
                tuplo = (x,y)
                if tuplo in coord: continue
                coord.append(tuplo)
                lista.append(x)
                lista.append(y)
            f.write(" ".join(str(item) for item in lista) + "\n")

gen()
