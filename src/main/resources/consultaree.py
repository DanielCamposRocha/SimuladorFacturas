# consulta_precio.py
import requests
import sys
import json

def obtener_precios(start_date , end_date , token ):
    url = "https://apidatos.ree.es/es/datos/mercados/precios-mercados-tiempo-real"
    params = {
        'start_date': start_date,
        'end_date': end_date,
        'time_trunc': 'hour'
    }
    headers = {
        'Authorization': f'Bearer {token}',
        'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36',
        'Accept': 'application/json',
        'Accept-Language': 'en-US,en;q=0.5',
        'Connection': 'keep-alive'
    }

    response = requests.get(url, params=params, headers=headers)
    
    if response.status_code == 200:
        return response.json()
    else:
        return {'error': response.status_code, 'message': response.text}

if __name__ == "__main__":
   start_date = sys.argv[1]
   end_date = sys.argv[2]
   token = sys.argv[3]

   resultado = obtener_precios(start_date, end_date, token)
    #resultado = obtener_precios()
   print(json.dumps(resultado))
