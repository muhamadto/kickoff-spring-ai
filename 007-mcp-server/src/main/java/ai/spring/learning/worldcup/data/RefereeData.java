/*
 * Licensed to Muhammad Hamadto 2026
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * See the NOTICE file distributed with this work for additional information regarding copyright ownership.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ai.spring.learning.worldcup.data;

import ai.spring.learning.worldcup.model.Referee;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * The 51 named main referees confirmed from FIFA's appointed list of 52 for the 2026 World Cup; one seat was not distinctly named in the source
 * consulted, so it is left out rather than invented.
 */
@Component
public class RefereeData {

	private static final List<Referee> REFEREES = List.of(new Referee("Omar Al Ali", "United Arab Emirates"),
			new Referee("Abdulrahman Al-Jassim", "Qatar"), new Referee("Khalid Al-Turais", "Saudi Arabia"), new Referee("Alireza Faghani", "Australia"),
			new Referee("Ma Ning", "China"), new Referee("Adham Makhadmeh", "Jordan"), new Referee("Ilgiz Tantashev", "Uzbekistan"),
			new Referee("Yusuke Araki", "Japan"), new Referee("Pierre Atcho", "Gabon"), new Referee("Dahane Beida", "Mauritania"),
			new Referee("Mustapha Ghorbal", "Algeria"), new Referee("Jalal Jayed", "Morocco"), new Referee("Amin Omar", "Egypt"),
			new Referee("Abongile Tom", "South Africa"), new Referee("Iván Barton", "El Salvador"), new Referee("Juan Gabriel Calderón", "Costa Rica"),
			new Referee("Ismail Elfath", "United States"), new Referee("Oshane Nation", "Jamaica"), new Referee("Drew Fischer", "Canada"),
			new Referee("Katia Itzel García", "Mexico"), new Referee("Saíd Martínez", "Honduras"), new Referee("Tori Penso", "United States"),
			new Referee("César Arturo Ramos", "Mexico"), new Referee("Ramon Abatti", "Brazil"), new Referee("Juan Gabriel Benítez", "Paraguay"),
			new Referee("Raphael Claus", "Brazil"), new Referee("Yael Falcón", "Argentina"), new Referee("Cristián Garay", "Chile"),
			new Referee("Darío Herrera", "Argentina"), new Referee("Kevin Ortega", "Peru"), new Referee("Andrés Rojas", "Colombia"),
			new Referee("Wilton Sampaio", "Brazil"), new Referee("Gustavo Tejera", "Uruguay"), new Referee("Facundo Tello", "Argentina"),
			new Referee("Jesús Valenzuela", "Venezuela"), new Referee("Campbell-Kirk Kawana-Waugh", "New Zealand"), new Referee("Espen Eskås", "Norway"),
			new Referee("Alejandro Hernández Hernández", "Spain"), new Referee("István Kovács", "Romania"), new Referee("François Letexier", "France"),
			new Referee("Danny Makkelie", "Netherlands"), new Referee("Szymon Marciniak", "Poland"), new Referee("Maurizio Mariani", "Italy"),
			new Referee("Glenn Nyberg", "Sweden"), new Referee("Michael Oliver", "England"), new Referee("João Pinheiro", "Portugal"),
			new Referee("Sandro Schärer", "Switzerland"), new Referee("Anthony Taylor", "England"), new Referee("Clément Turpin", "France"),
			new Referee("Slavko Vinčić", "Slovenia"), new Referee("Felix Zwayer", "Germany"));

	public List<Referee> referees() {
		return REFEREES;
	}
}
