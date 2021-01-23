#!/usr/bin/env python
import customer_pb2
import json, requests

customer = customer_pb2.Customer()

def print_all():
    resp = requests.request("GET", "http://localhost:8080/clients/")
    print('Клиенты в базе:')
    for k in resp.json():
        print(k)


def serialize(listClients):
    for i in range(len(listClients)):
        listClients[i] = listClients[i].__dict__
    return listClients


def requesting(clients):
    print_all()
    n = input('Добавить клиентов из файла? (да/нет): ')
    if "Д" in n.upper():
        clients = serialize(clients)
        resp = requests.request("POST", "http://localhost:8080/clients/addMany", data=clients)
        print(resp.text)
        print_all()
    flag = True
    while flag:
        n = input('Удалить клиента из базы? (да/нет): ')
        if "Д" in n.upper():
            try:
                id = int(input('Введите id клиента, которого хотите удалить: '))
                resp = requests.request("DELETE", f"http://localhost:8080/clients/{id}")
                print(resp.text)
            except ValueError:
                print("Некорректный id!")
        elif "Н" in n.upper():
            flag = False
        else:
            print('Вы ввели что-то не то, попробуйте ещё раз')


def main():
    f = open('clients.txt', 'r')
    from_file = f.read()
    f.close()
    j = json.loads(from_file)
    clients = []
    for i in j:
        clients.append(customer(i['name'], i['email'], i['age'], i['educated'], i['birth_date'], i['growth']))
    requesting(clients)


if __name__ == '__main__':
    main()
