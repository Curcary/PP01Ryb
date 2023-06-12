using Microsoft.Win32;
using System;
using System.Collections.Generic;
using System.Linq;
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
using The_Testo.Database;
using The_Testo.Printers;

namespace The_Testo.Pages
{
    /// <summary>
    /// Логика взаимодействия для CurrentOrdersPage.xaml
    /// </summary>
    public partial class CurrentOrdersPage : Page
    {
        public CurrentOrdersPage()
        {
            InitializeComponent();
        }

        private void Page_Loaded(object sender, RoutedEventArgs e)
        {
            OrderView.ItemsSource = DB.entities.Order.Where(s=>s.CheckStatus).ToList();
        }

        private void PrintButton_Click(object sender, RoutedEventArgs e)
        {
            
            if (OrderView.SelectedItem is Order check)
            {
                Print();
            };
        }

        private void Print()
        {
            if (OrderView.SelectedItem is Order order)
            {
                SaveFileDialog saveFileDialog = new SaveFileDialog();
                saveFileDialog.Filter = "Word Documents|*.docx";
                if (saveFileDialog.ShowDialog().Value)
                {
                    order.CheckStatus = false;
                    string path = saveFileDialog.FileName; 
                    path.Replace(@"\", "\\");
                    CheckPrinter.PrintCheque(order, path);
                    DB.entities.SaveChanges();

                }
            }
            Check();
        }

        private void TextBlock_MouseUp(object sender, MouseButtonEventArgs e)
        {
            if (OrderView.SelectedItem is Order order)
            {
                NavigationService.Navigate(new AddEditOrderPage(order));
            }
            
        }
        private void Check()
        {
            if (ShowEverythingToggleButton.IsChecked.Value)
            {
                OrderView.ItemsSource = DB.entities.Order.ToList();
            }
            else
                OrderView.ItemsSource = DB.entities.Order.Where(s => s.CheckStatus).ToList();
        }
        private void ToggleButton_Checked(object sender, RoutedEventArgs e)
        {
            Check();
        }

    }
}
