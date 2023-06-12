using System;
using System.Collections.Generic;
using System.Data.Entity.Core.Objects;
using System.Linq;
using System.Reflection.Emit;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using LiveCharts;
using LiveCharts.Wpf;
using The_Testo.Database;

namespace The_Testo.Pages
{
    /// <summary>
    /// Логика взаимодействия для StatPage.xaml
    /// </summary>
    public partial class StatPage : Page
    {
        public SeriesCollection SeriesCollection { get; set; }
        public StatPage()
        {
            InitializeComponent();
            DataContext = this;
        }
        //Get list of values with procedure
        private List<decimal> getValues(int days, string month)
        {
            List<decimal> values = new List<decimal>();
            try
            {
                for (int i = 0; i < days; i++)
                {
                    string datestr = (i + 1).ToString() + " "
                        + month
                        + " " + YearTextBox.Text;

                    DateTime dt = DateTime.Parse(datestr);
                    Type type = decimal.Zero.GetType();
                    ObjectParameter @object = new ObjectParameter("OutMoney", type);
                    DB.entities.DaySummary(dt, @object);
                    if (@object.Value.ToString() != "")
                        values.Add(decimal.Parse(@object.Value.ToString()));
                    else values.Add(0);
                }
            }
            catch (FormatException ex)
            {
                MessageBox.Show(ex.Message);
            }
            return values;
        }
        private void ShowStatistics()
        {
            if (FirstMonthComboBox != null && SecondMonthComboBox != null && YearTextBox!=null){
                //Get count of days of selected month
                int days1 = DateTime.DaysInMonth(DateTime.Now.Year, FirstMonthComboBox.SelectedIndex + 1);
                int days2 = DateTime.DaysInMonth(DateTime.Now.Year, SecondMonthComboBox.SelectedIndex + 1);
                string month1 = ((ComboBoxItem)FirstMonthComboBox.SelectedItem).Content.ToString();
                string month2 = ((ComboBoxItem)SecondMonthComboBox.SelectedItem).Content.ToString();
                List<decimal> values1 = getValues(days1, month1);
                values1.Insert(0, 0);
                List<decimal> values2 = getValues(days2, month2);
                values2.Insert(0, 0);  
                SeriesCollection = new SeriesCollection ()
                {
                    new LineSeries()
                    {
                        Title = month1,
                        Values = new ChartValues<decimal>(values1),
                        Fill = new SolidColorBrush(Colors.Transparent)
                    },
                    new LineSeries()
                    {
                        Title = month2,
                        Values = new ChartValues<decimal>(values2), 
                        Fill = new SolidColorBrush(Colors.Transparent)


                    }
                };
            }
            Chart.Series = SeriesCollection;
        }

        private void YearTextBox_PreviewTextInput(object sender, TextCompositionEventArgs e)
        {
            foreach (var item in e.Text)
            {
                if (!char.IsDigit(item))
                {
                    e.Handled = true;
                    break;
                }
            }
        }


        private void ShowStatButton_Click(object sender, RoutedEventArgs e)
        {
            ShowStatistics();
        }
    }
}
