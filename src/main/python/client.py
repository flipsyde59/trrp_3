#!/usr/bin/env python

import requests as req
import customer_pb2

customer = customer_pb2.Customer()
listCustomers = customer_pb2.ListCustomers()
emailAddress = customer_pb2.Customer.EmailAddress()


def read_from_file():
    file = open('clients.txt')
    for line in file.read().split('\n'):
        number_line = 1
        l = line.split(', ')
        customer.id = 0
        customer.firstName = l[1]
        customer.lastName = l[2]
        emailAddress.email = l[3]
        customer.emails.append(emailAddress)
        resp = req.request('POST', 'http://localhost:8080/customers', data=customer.SerializeToString(),
                           headers={'X-Protobuf-Schema': 'customer.proto',
                                    'X-Protobuf-Message': 'trrp_3.Customer',
                                    'Content-Type': 'application/x-protobuf;charset=UTF-8'})
        if resp.ok:
            print(f"Строка {number_line} статус: внесено")
        else:
            print(f"Строка {number_line} статус: ошибка")
        number_line += 1
    file.close()


def update(id):
    resp = req.request('PUT', f'http://localhost:8080/customers/{id}', data=customer.SerializeToString(),
                       headers={'X-Protobuf-Schema': 'customer.proto',
                                'X-Protobuf-Message': 'trrp_3.Customer',
                                'Content-Type': 'application/x-protobuf;charset=UTF-8'})
    if resp.ok:
        print("Данные внесены")


def requests():
    resp = req.request("GET", 'http://localhost:8080/customers')
    listCustomers.ParseFromString(resp.content)
    if len(listCustomers.items)>0:
        print("Данные в базе:\n", listCustomers.items)
    else:
        print("В базе на сервере пусто :(")
    n = input("Внести данные из файла? (Да/Нет): ")
    if "Д" in n.upper():
        read_from_file()
    n = input("Вывести данные одного клиента? (Да/Нет): ")
    if "Д" in n.upper():
        id = int(input("Введите id клиента: "))
        resp = req.request("GET", f'http://localhost:8080/customers/{id}')
        if resp.ok:
            customer.ParseFromString(resp.content)
            print(customer)
            n = input("Обновить данные клиента? (Да/Нет): ")
            if "Д" in n.upper():
                flag = True
                while flag:
                    fields = input(
                        "Какие поля обновить? (1-имя, 2-фамилия, 3-email, 4-добавить email, 0-выход из меню обновления): ")
                    if fields == '1':
                        customer.firstName = input("Введите новое имя: ")
                        update(id)
                    elif fields == '2':
                        customer.lastName = input("Введите новую фамилию: ")
                        update(id)
                    elif fields == '3':
                        customer.emails[0].email = input("Введите новый email: ")
                        update(id)
                    elif fields == '4':
                        emailAddress.email = input("Введите новый email: ")
                        type = None
                        while type!='1' and type!='2':
                            type = input("Введите тип нового email'a (1-рабочий, 2-личный): ")
                            if type!='1' and type!='2':
                                print("Вы ввели не то значение, попробуйте ещё раз")
                        emailAddress.type = int(type)- 1
                        customer.emails.append(emailAddress)
                        update(id)
                    else:
                        print("Выход из меню")
                        flag = False
                        resp = req.request("GET", 'http://localhost:8080/customers')
                        listCustomers.ParseFromString(resp.content)
                        print("Данные в базе:\n", listCustomers.items)
        else:
            print("Не удалось получить данные")
    n = input("Удалить клиента? (Да/Нет): ")
    if "Д" in n.upper():
        id = int(input("Введите id клиента: "))
        resp = req.request("DELETE", f'http://localhost:8080/customers/{id}')
        if resp.ok:
            print("Клиент удалён")
        else:
            print("При удалении возникла ошибка")
    n = input("Повторить выполнение программы? (Да/Нет): ")
    if "Д" in n.upper():
        return True
    else:
        return False

if __name__ == '__main__':
    while requests():
        print()
